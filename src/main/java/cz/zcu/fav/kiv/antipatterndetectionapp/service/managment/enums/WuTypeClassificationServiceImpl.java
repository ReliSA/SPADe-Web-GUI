package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuTypeClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.WuTypeClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class WuTypeClassificationServiceImpl implements WuTypeClassificationService {

    @Autowired
    private WuTypeClassificationRepository wuTypeClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<WuTypeClassification> wuTypes = getAllWuTypeClasses();
        return new ArrayList<>(wuTypes);
    }

    @Override
    public List<WuTypeClassification> getAllWuTypeClasses() {
        List<WuTypeClassification> wuTypes = wuTypeClassificationRepository.findAll();
        Collections.sort(wuTypes, Comparator.comparing(WuTypeClassification::getId));
        return wuTypes;
    }

    @Override
    public Classification getClassById(Long id) {
        return wuTypeClassificationRepository.getOne(id);
    }
}
