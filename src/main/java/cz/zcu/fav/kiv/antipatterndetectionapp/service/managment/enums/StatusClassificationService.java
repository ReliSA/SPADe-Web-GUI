package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.StatusClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;

import java.util.List;

public interface StatusClassificationService extends Service {
    List<StatusClassification> getAllStatusClasses();
}
