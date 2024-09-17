package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.RoleClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;

import java.util.List;

public interface RoleClassificationService extends Service {
    List<RoleClassification> getAllRoleClasses();
}
