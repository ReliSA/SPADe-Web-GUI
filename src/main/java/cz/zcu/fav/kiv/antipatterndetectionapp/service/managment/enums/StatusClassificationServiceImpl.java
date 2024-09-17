package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.StatusClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.StatusClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class StatusClassificationServiceImpl implements StatusClassificationService {

    @Autowired
    private StatusClassificationRepository statusClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<StatusClassification> statuses = getAllStatusClasses();
        return new ArrayList<>(statuses);
    }

    @Override
    public List<StatusClassification> getAllStatusClasses() {
        List<StatusClassification> statuses = statusClassificationRepository.findAll();
        Collections.sort(statuses, Comparator.comparing(StatusClassification::getId));
        return statuses;
    }

    @Override
    public Classification getClassById(Long id) {
        return statusClassificationRepository.getOne(id);
    }
}
