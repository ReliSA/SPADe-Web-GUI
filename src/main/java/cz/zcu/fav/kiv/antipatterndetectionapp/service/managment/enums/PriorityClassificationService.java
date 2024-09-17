package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.PriorityClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;

import java.util.List;

public interface PriorityClassificationService extends Service {

    List<PriorityClassification> getAllPriorityClasses();
}
