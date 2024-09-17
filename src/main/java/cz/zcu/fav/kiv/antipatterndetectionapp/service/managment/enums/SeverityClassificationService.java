package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.SeverityClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;

import java.util.List;

public interface SeverityClassificationService extends Service {

    List<SeverityClassification> getAllSeverityClasses();
}
