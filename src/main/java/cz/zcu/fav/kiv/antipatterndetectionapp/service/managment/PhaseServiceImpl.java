package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public List<Phase> getAllPhases() {
        List<Phase> phases = phaseRepository.findAll();
        Collections.sort(phases, Comparator.comparing(Phase::getName, String.CASE_INSENSITIVE_ORDER));
        return phases;
    }

    @Override
    public Phase savePhase(Phase phase) {
        Phase newPhase = phaseRepository.save(phase);
        //phaseRepository.flush();
        return newPhase;
    }

    @Override
    public void deletePhases(List<Phase> phases) {
        phaseRepository.deleteAll(phases);
    }

    @Override
    public void deletePhase(Phase phase) {
        phaseRepository.delete(phase);
    }
}
