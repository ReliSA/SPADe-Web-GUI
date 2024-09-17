package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.PriorityClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.PriorityClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PriorityClassificationServiceImpl implements PriorityClassificationService {

    @Autowired
    private PriorityClassificationRepository priorityClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<PriorityClassification> priorities = getAllPriorityClasses();
        return new ArrayList<>(priorities);
    }

    @Override
    public List<PriorityClassification> getAllPriorityClasses() {
        List<PriorityClassification> priorities = priorityClassificationRepository.findAll();
        Collections.sort(priorities, Comparator.comparing(PriorityClassification::getId));
        return priorities;
    }

    @Override
    public Classification getClassById(Long id) {
        return priorityClassificationRepository.getOne(id);
    }
}
