package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;

import java.util.List;

public interface ProjectInstanceService {

    /**
     * Method getting all project instances from database
     * @return  List of all project instances in database
     */
    List<ProjectInstance> getAllProjectInstances();
}
