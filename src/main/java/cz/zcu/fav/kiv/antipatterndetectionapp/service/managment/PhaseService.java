package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;

import java.util.List;

public interface PhaseService {

    /**
     * Method getting all phases from database
     * @return  List of all phases in database
     */
    List<Phase> getAllPhases();

    /**
     * Method saving phase into database
     * @param phase     Phase tha will be safe
     * @return          Phase saved in database
     */
    Phase savePhase(Phase phase);

    /**
     * Method for deleting phases from database
     * @param phases    List of phases for deletion
     */
    void deletePhases(List<Phase> phases);

    /**
     * Method for deleting phase from database
     * @param phase    Phase for deletion
     */
    void deletePhase(Phase phase);
}
