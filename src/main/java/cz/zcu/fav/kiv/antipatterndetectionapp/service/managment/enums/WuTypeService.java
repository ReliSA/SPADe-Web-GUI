package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuType;

import java.util.List;

public interface WuTypeService extends EnumService {

    List<WuType> getAllWuTypes();

    WuType getWuTypeById(Long id);
}
