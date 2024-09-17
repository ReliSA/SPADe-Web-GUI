package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;

import java.util.List;

public interface Service {

    /**
     * Method getting all classes of enum type
     * @return  List of classifications
     */
    List<Classification> getAllClasses();

    /**
     * Method getting class by ID
     * @param id    ID of classification
     * @return      Classification with that ID
     */
    Classification getClassById(Long id);
}
