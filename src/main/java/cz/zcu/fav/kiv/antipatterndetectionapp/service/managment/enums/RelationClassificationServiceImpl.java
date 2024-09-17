package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.RelationClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.RelationClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RelationClassificationServiceImpl implements RelationClassificationService {

    @Autowired
    private RelationClassificationRepository relationClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<RelationClassification> relations = getAllRelationClasses();
        Collections.sort(relations, Comparator.comparing(RelationClassification::getSuperClass, String.CASE_INSENSITIVE_ORDER));
        return new ArrayList<>(relations);
    }

    @Override
    public List<RelationClassification> getAllRelationClasses() {
        List<RelationClassification> relations = relationClassificationRepository.findAll();
        Collections.sort(relations, Comparator.comparing(RelationClassification::getId));
        return relations;
    }

    @Override
    public Classification getClassById(Long id) {
        return relationClassificationRepository.getOne(id);
    }
}
