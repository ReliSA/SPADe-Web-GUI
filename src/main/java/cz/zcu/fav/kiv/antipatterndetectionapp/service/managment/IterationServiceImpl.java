package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.IterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class IterationServiceImpl implements IterationService {

    @Autowired
    private IterationRepository iterationRepository;

    @Override
    public List<Iteration> getAllIterations() {
        List<Iteration> iterations = iterationRepository.findAll();
        Collections.sort(iterations, Comparator.comparing(Iteration::getName, String.CASE_INSENSITIVE_ORDER));
        return iterations;
    }

    @Override
    public Iteration saveIteration(Iteration iteration) {
        Iteration newIteration = iterationRepository.save(iteration);
        iterationRepository.flush();
        return newIteration;
    }

    @Override
    public void deleteIterations(List<Iteration> iterations) {
        iterationRepository.deleteAll(iterations);
    }

    @Override
    public void deleteIteration(Iteration iteration) {
        iterationRepository.delete(iteration);
    }


}
