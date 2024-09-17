package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Status;

import java.util.List;

public interface StatusService extends EnumService {
    List<Status> getAllStatuses();
}
