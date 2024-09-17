package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.SeverityClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.SeverityClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class SeverityClassificationServiceImpl implements SeverityClassificationService{

    @Autowired
    private SeverityClassificationRepository severityClassificationRepository;

    @Override
    public List<SeverityClassification> getAllSeverityClasses() {
        List<SeverityClassification> severities = severityClassificationRepository.findAll();
        Collections.sort(severities, Comparator.comparing(SeverityClassification::getId));
        return severities;
    }

    @Override
    public List<Classification> getAllClasses() {
        List<SeverityClassification> severities = getAllSeverityClasses();
        return new ArrayList<>(severities);
    }

    @Override
    public Classification getClassById(Long id) {
        return severityClassificationRepository.getOne(id);
    }
}
