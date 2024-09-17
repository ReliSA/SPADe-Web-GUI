package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;

import java.util.List;

public interface IterationService {

    /**
     * Method getting all iterations from database
     * @return  List of all iterations in database
     */
    List<Iteration> getAllIterations();

    /**
     * Method saving iteration into database
     * @param iteration Iteration tha will be safe
     * @return          Iteration saved in database
     */
    Iteration saveIteration(Iteration iteration);

    /**
     * Method for deleting iterations from database
     * @param iterations    List of iterations for deletion
     */
    void deleteIterations(List<Iteration> iterations);

    /**
     * Method for deleting iteration from database
     * @param iteration    Iteration for deletion
     */
    void deleteIteration(Iteration iteration);
}
