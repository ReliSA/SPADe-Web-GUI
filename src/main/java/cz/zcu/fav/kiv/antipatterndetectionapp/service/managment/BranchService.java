package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Branch;

import java.util.List;

public interface BranchService {

    /**
     * Method for getting all branches from database
     * @return  List of all branches in databse
     */
    List<Branch> getAllBranches();

    /**
     * Method getting branch by id
     * @param id    ID of branch
     * @return      Branch with that ID
     */
    Branch getBranchById(Long id);
}
