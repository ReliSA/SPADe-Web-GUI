package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Resolution;

import java.util.List;

public interface ResolutionService extends EnumService {
    List<Resolution> getAllResolutions();
}
