package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Relation;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationRepository relationRepository;

    @Override
    public List<Relation> getAllRelations() {
        List<Relation> relations = relationRepository.findAll();
        Collections.sort(relations, Comparator.comparing(Relation::getName, String.CASE_INSENSITIVE_ORDER));
        return relations;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return relationRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Relation newEnum = relationRepository.save((Relation) enumType);
        relationRepository.flush();
        return newEnum;
    }
}
