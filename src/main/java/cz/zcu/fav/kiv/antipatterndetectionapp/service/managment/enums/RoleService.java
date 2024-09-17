package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Role;

import java.util.List;

public interface RoleService extends EnumService {
    List<Role> getAllRoles();
}
