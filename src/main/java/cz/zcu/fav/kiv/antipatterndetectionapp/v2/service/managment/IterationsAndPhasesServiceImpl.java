package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.managment;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.IterationRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.PhaseRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.IterationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PhaseService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationAndPhasesDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PhaseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationAndPhasesToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PhaseToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IterationsAndPhasesServiceImpl implements IterationsAndPhasesService {

    @Autowired
    private IterationRepository iterationRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private WorkUnitService workUnitService;

    @Autowired
    private IterationService iterationService;

    @Override
    public ResponseEntity<String> getIterationAndPhases(Project project) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Iteration> iterations = project.getIterations();
        List<Phase> phases = project.getPhases();

        List<IterationDto> iterationDtos = new IterationToDto().convert(iterations);
        List<PhaseDto> phasesDtos = new PhaseToDto().convert(phases);


        Collections.sort(iterationDtos, Comparator.comparing(IterationDto::getName, String.CASE_INSENSITIVE_ORDER));
        Collections.sort(phasesDtos, Comparator.comparing(PhaseDto::getName, String.CASE_INSENSITIVE_ORDER));

        IterationAndPhasesDto iterationAndPhasesDto = new IterationAndPhasesToDto().convert(iterationDtos, phasesDtos);

        try {
            String json = objectMapper.writeValueAsString(iterationAndPhasesDto);
            return ResponseEntity.status((iterations != null && phases != null) ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to serialize data to JSON");
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeToPhase(String[] iterationsIds) {
        List<Long> iterationIds = new ArrayList<>();
        for (String id : iterationsIds)
                iterationIds.add(Long.parseLong(id));
        List<Iteration> selectedIterations = iterationRepository.fetchIterationsByIds(iterationIds);


        StringBuilder message = new StringBuilder();
        int errors = 0;

        for (Iteration iteration : selectedIterations) {
            message.append("Iteration: ").append(iteration.getName()).append(System.getProperty("line.separator"));

            if (iteration.getWorkUnits().size() == 0) {
                errors += 1;
                message.append(Constants.INDENT)
                        .append("cannot be changed to Phase due to non-existent work unit")
                        .append(System.getProperty("line.separator"))
                        .append(System.getProperty("line.separator"));
                continue;
            }

            Phase phase = null;
            for (WorkUnit workUnit : iteration.getWorkUnits()) { // If exists WorkUnit with empty Phase - create Phase from Iteration
                if (workUnit.getPhase() == null) {
                    phase = new Phase(iteration.getExternalId(),
                            iteration.getName(),
                            iteration.getDescription(),
                            iteration.getEndDate(),
                            iteration.getStartDate(),
                            iteration.getCreated(),
                            iteration.getSuperProjectId());
                    phaseService.savePhase(phase);
                    break;
                }
            }

            for (WorkUnit workUnit : iteration.getWorkUnits()) { // For every WorkUnit with empty Phase - assign created Phase
                Phase workUnitPhrase = workUnit.getPhase();
                if (workUnitPhrase == null) {
                    workUnit.setPhase(phase);
                    message.append(Constants.INDENT)
                            .append("successfully assigned to WU with id ").append(workUnit.getId())
                            .append(System.getProperty("line.separator"));
                    workUnitService.saveWorkUnit(workUnit);

                } else {
                    errors += 1;
                    message.append(Constants.INDENT)
                            .append("cannot be assigned to WU with id ").append(workUnit.getId())
                            .append(" as it already exists with Phase ").append(workUnit.getPhase().getName())
                            .append(System.getProperty("line.separator"));
                }
                if (phase != null) {
                    workUnit.setIteration(null);
                }
                if(phase != null || workUnitPhrase == null){
                    workUnitService.saveWorkUnit(workUnit);
                }
            }
            message.append(System.getProperty("line.separator"));
            if (phase != null) {
                iteration.setWorkUnits(null);
                iterationService.deleteIteration(iteration);
            }
        }

        Map<String,Object> json = new HashMap<>();
        json.put("message", message);

        if (errors == 0) {
            json.put("successMessage", "All selected iterations (" + selectedIterations.size() + ") were transformed to phases");
        } else {
            json.put("informMessage", "Some iterations cannot be assign, look at the log for more information");
        }

        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    //TODO kontrola, proc tenhle zhavej odpad nefunguje
    @Override
    public ResponseEntity<Map<String, Object>> changeToIteration(String[] phasesIds) {
        List<Long> phaseIds = new ArrayList<>();
        for (String id : phasesIds)
            phaseIds.add(Long.parseLong(id));
        List<Phase> selectedPhases = phaseRepository.fetchPhasesByIds(phaseIds);

        StringBuilder message = new StringBuilder();
        int errors = 0;

        for (Phase phase : selectedPhases) {
            message.append("Phase: ").append(phase.getName()).append(System.getProperty("line.separator"));

            if (phase.getWorkUnits().size() == 0) {
                errors += 1;
                message.append(Constants.INDENT)
                        .append("cannot be changed to Phase due to non-existent work unit")
                        .append(System.getProperty("line.separator"))
                        .append(System.getProperty("line.separator"));
                continue;
            }

            Iteration iteration = null;
            for (WorkUnit workUnit : phase.getWorkUnits()) { // If exists WorkUnit with empty Phase - create Phase from Iteration
                if (workUnit.getIteration() == null) {
                    iteration = new Iteration(phase.getExternalId(),
                            phase.getName(),
                            phase.getDescription(),
                            phase.getEndDate(),
                            phase.getStartDate(),
                            phase.getCreated(),
                            phase.getSuperProjectId());
                    iterationService.saveIteration(iteration);
                    break;
                }
            }

            for (WorkUnit workUnit : phase.getWorkUnits()) { // For every WorkUnit with empty Phase - assign created Phase
                if (workUnit.getIteration() == null) {
                    workUnit.setIteration(iteration);
                    workUnitService.saveWorkUnit(workUnit);
                    message.append(Constants.INDENT)
                            .append("successfully assigned to WU with id ").append(workUnit.getId())
                            .append(System.getProperty("line.separator"));
                } else {
                    errors += 1;
                    message.append(Constants.INDENT)
                            .append("cannot be assigned to WU with id ").append(workUnit.getId())
                            .append(" as it already exists with Iteration ").append(workUnit.getIteration().getName())
                            .append(System.getProperty("line.separator"));
                }
                if (iteration != null) {
                    workUnit.setPhase(null);
                }
                workUnitService.saveWorkUnit(workUnit);
            }
            message.append(System.getProperty("line.separator"));
            if (iteration != null) {
                phase.setWorkUnits(null);
                phaseService.deletePhase(phase);
            }
        }

        Map<String,Object> json = new HashMap<>();
        json.put("message", message);

        if (errors == 0) {
            json.put("successMessage", "All selected iterations (" + selectedPhases.size() + ") were transformed to phases");
        } else {
            json.put("informMessage", "Some iterations cannot be assign, look at the log for more information");
        }

        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

}
