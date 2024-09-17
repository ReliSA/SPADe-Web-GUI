package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;

public interface EnumService {

    /**
     * Method getting enum by ID
     * @param id    ID of enum
     * @return      EnumType instance
     */
    EnumType getEnumById(Long id);

    /**
     * Method saving EnumType instance
     * @param enumType  Instance for save
     * @return          Saved instance of EnumType
     */
    EnumType saveEnum(EnumType enumType);
}
