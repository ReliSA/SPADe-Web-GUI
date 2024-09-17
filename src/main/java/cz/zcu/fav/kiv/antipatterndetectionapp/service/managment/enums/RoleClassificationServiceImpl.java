package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.RoleClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.RoleClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RoleClassificationServiceImpl implements RoleClassificationService {

    @Autowired
    private RoleClassificationRepository roleClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<RoleClassification> roles = getAllRoleClasses();
        return new ArrayList<>(roles);
    }

    @Override
    public List<RoleClassification> getAllRoleClasses() {
        List<RoleClassification> roles = roleClassificationRepository.findAll();
        Collections.sort(roles, Comparator.comparing(RoleClassification::getId));
        return roles;
    }

    @Override
    public Classification getClassById(Long id) {
        return roleClassificationRepository.getOne(id);
    }
}
