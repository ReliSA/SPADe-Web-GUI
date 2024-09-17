package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;

import java.util.List;

public interface WorkUnitService {

    /**
     * Method getting all work units from database
     * @return  List of all work units in database
     */
    List<WorkUnit> getAllProjects();

    /**
     * Method saving work unit into database
     * @param workUnit  WorkUnit tha will be safe
     * @return          WorkUnit saved in database
     */
    WorkUnit saveWorkUnit(WorkUnit workUnit);
}
