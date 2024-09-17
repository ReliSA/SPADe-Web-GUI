package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Priority;

import java.util.List;

public interface PriorityService extends EnumService {

    List<Priority> getAllPriorities();
}
