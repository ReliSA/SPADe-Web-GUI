package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Role;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        Collections.sort(roles, Comparator.comparing(Role::getName, String.CASE_INSENSITIVE_ORDER));
        return roles;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Role newEnum = roleRepository.save((Role) enumType);
        roleRepository.flush();
        return newEnum;
    }
}
