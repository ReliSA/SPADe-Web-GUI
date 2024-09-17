package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.ResolutionClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;

import java.util.List;

public interface ResolutionClassificationService extends Service {
    List<ResolutionClassification> getAllResolutionClasses();
}
