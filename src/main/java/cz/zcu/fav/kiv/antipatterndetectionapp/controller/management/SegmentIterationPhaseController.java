package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.IterationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PhaseService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This class contains all endpoints of segment-iteration-phase.html
 */
@Controller
public class SegmentIterationPhaseController {

    private final Logger LOGGER = LoggerFactory.getLogger(SegmentIterationPhaseController.class);

    @Autowired
    private IterationService iterationService;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkUnitService workUnitService;

    /**
     * Method for showing iteration and phase page for specific project
     * @param model         Object for passing data to the UI
     * @param project       Selected project for showing categories
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @GetMapping("/management/segment-iteration-phase")
    public String changeProject(Model model,
                                @RequestParam(value = "selectedProject", required = false) Long project,
                                RedirectAttributes redirectAttrs,
                                HttpSession session) {

        // First open in session
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/segment-iteration-phase\") - Accessing page");
            return "management/segment-iteration-phase";
        }

        // Project selected
        if(project != null) {
            session.setAttribute("project", project);
            Utils.sessionRecreate(session);
        }

        project = (long) session.getAttribute("project");

        Project foundedProject = projectService.getProjectById(project);
        if(foundedProject == null) {
            if(!Objects.equals(project, Constants.DEFAULT_ID)) {
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Project not found");
                LOGGER.info("@GetMapping(\"/management/segment-iteration-phase\")- Project not found");
            } else {
                LOGGER.info("@GetMapping(\"/management/segment-iteration-phase\") - Accessing page");
            }
            model.addAttribute("projects", projectService.getAllProjects());
            return "management/segment-iteration-phase";
        }

        List<Iteration> iterations = foundedProject.getIterations();
        List<Phase> phases = foundedProject.getPhases();
        Collections.sort(iterations, Comparator.comparing(Iteration::getName, String.CASE_INSENSITIVE_ORDER));
        Collections.sort(phases, Comparator.comparing(Phase::getName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("iterations", iterations);
        model.addAttribute("phases", phases);
        model.addAttribute("projects", projectService.getAllProjects());
        LOGGER.info("@GetMapping(\"/management/segment-iteration-phase\") - Accessing page");

        return "/management/segment-iteration-phase";
    }

    /**
     * Method for changing iteration to phase
     * @param model                 Object for passing data to the UI
     * @param selectedIterations    Iterations selected by user for change
     * @param redirectAttrs         Attributes for redirection
     * @return                      HTML template
     */
    @PostMapping(value = "/changeIteration")
    public String changeToPhase(Model model,
                                @RequestParam(value = "selectedIterations", required = false) List<Iteration> selectedIterations,
                                RedirectAttributes redirectAttrs) {

        if(selectedIterations == null || selectedIterations.isEmpty()) {

            model.addAttribute("iterations", iterationService.getAllIterations());
            model.addAttribute("phases", phaseService.getAllPhases());
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected before submission");
            LOGGER.info("@PostMapping(\"/management/segment-iteration-phase\") - Cannot proceed: Submission went wrong");

        } else {

            StringBuilder message = new StringBuilder();
            int errors = 0;

            for(Iteration iteration : selectedIterations) {
                message.append("Iteration: ").append(iteration.getName()).append(System.getProperty("line.separator"));

                if(iteration.getWorkUnits().size() == 0) {
                    errors += 1;
                    message.append(Constants.INDENT)
                            .append("cannot be changed to Phase due to non-existent work unit")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));
                    continue;
                }

                Phase phase = null;
                for(WorkUnit workUnit : iteration.getWorkUnits()) { // If exists WorkUnit with empty Phase - create Phase from Iteration
                    if(workUnit.getPhase() == null) {
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

                for(WorkUnit workUnit : iteration.getWorkUnits()) { // For every WorkUnit with empty Phase - assign created Phase
                    if(workUnit.getPhase() == null) {
                        workUnit.setPhase(phase);
                        message.append(Constants.INDENT)
                                .append("successfully assigned to WU with id ").append(workUnit.getId())
                                .append(System.getProperty("line.separator"));
                    } else {
                        errors += 1;
                        message.append(Constants.INDENT)
                                .append("cannot be assigned to WU with id ").append(workUnit.getId())
                                .append(" as it already exists with Phase ").append(workUnit.getPhase().getName())
                                .append(System.getProperty("line.separator"));
                    }
                    if(phase != null) {
                        workUnit.setIteration(null);
                    }
                    workUnitService.saveWorkUnit(workUnit);
                }
                message.append(System.getProperty("line.separator"));
                if(phase != null) {
                    iteration.setWorkUnits(null);
                    iterationService.deleteIteration(iteration);
                }
            }

            if(errors == 0) {
                redirectAttrs.addFlashAttribute("successMessage", "All selected iterations (" + selectedIterations.size() + ") were transformed to phases");
            } else {
                redirectAttrs.addFlashAttribute("informMessage", "Some iterations cannot be assign, look at the log for more information");
            }
            redirectAttrs.addFlashAttribute("message", message);
            LOGGER.info("@PostMapping(\"/management/segment-iteration-phase\") - Iterations were changed");
        }

        return "redirect:/management/segment-iteration-phase";
    }

    /**
     * Method for changing phase to iteration
     * @param model                 Object for passing data to the UI
     * @param selectedPhases        Phases selected by user for change
     * @param redirectAttrs         Attributes for redirection
     * @return                      HTML template
     */
    @PostMapping(value = "/changePhase")
    public String changeToIteration(Model model,
                                    @RequestParam(value = "selectedPhases", required = false) List<Phase> selectedPhases,
                                    RedirectAttributes redirectAttrs) {

        if(selectedPhases == null || selectedPhases.isEmpty()) {

            model.addAttribute("iterations", iterationService.getAllIterations());
            model.addAttribute("phases", phaseService.getAllPhases());
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected before submission");
            LOGGER.info("@PostMapping(\"/management/segment-iteration-phase\") - Cannot proceed: Submission went wrong");

        } else {

            StringBuilder message = new StringBuilder();
            int errors = 0;

            for(Phase phase : selectedPhases) {
                message.append("Phase: ").append(phase.getName()).append(System.getProperty("line.separator"));

                if(phase.getWorkUnits().size() == 0) {
                    errors += 1;
                    message.append(Constants.INDENT)
                            .append("cannot be changed to Phase due to non-existent work unit")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));
                    continue;
                }

                Iteration iteration = null;
                for(WorkUnit workUnit : phase.getWorkUnits()) { // If exists WorkUnit with empty Phase - create Phase from Iteration
                    if(workUnit.getIteration() == null) {
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

                for(WorkUnit workUnit : phase.getWorkUnits()) { // For every WorkUnit with empty Phase - assign created Phase
                    if(workUnit.getIteration() == null) {
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
                    if(iteration != null) {
                        workUnit.setPhase(null);
                    }
                    workUnitService.saveWorkUnit(workUnit);
                }
                message.append(System.getProperty("line.separator"));
                if(iteration != null) {
                    phase.setWorkUnits(null);
                    phaseService.deletePhase(phase);
                }
            }

            if(errors == 0) {
                redirectAttrs.addFlashAttribute("successMessage", "All selected phases (" + selectedPhases.size() + ") were transformed to iterations");
            } else {
                redirectAttrs.addFlashAttribute("informMessage", "Some phases cannot be assign, look at the log for more information");
            }
            redirectAttrs.addFlashAttribute("message", message);
            LOGGER.info("@PostMapping(\"/management/segment-iteration-phase\") - Phases were changed");
        }

        return "redirect:/management/segment-iteration-phase";
    }
}
