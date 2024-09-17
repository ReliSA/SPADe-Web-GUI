package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Severity;

import java.util.List;

public interface SeverityService extends EnumService {

    List<Severity> getAllSeverities();
}
