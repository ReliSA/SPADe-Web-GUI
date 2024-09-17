package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Commit;

import java.util.List;

public interface CommitService {

    /**
     * Method getting all commits from database
     * @return  List of all commits in database
     */
    List<Commit> getAllCommits();

    /**
     * Method getting commit by ID
     * @param id    ID of commit
     * @return      Commit with that ID
     */
    Commit getCommitById(Long id);

    /**
     * Method saving commit into database
     * @param commit    Commit tha will be safe
     * @return          Commit saved in database
     */
    Commit saveCommit(Commit commit);

    /**
     * Method getting released commits by project ID
     * @param id    Project's ID of released commits
     * @return      List of released commits in project
     */
    List<Commit> getReleasedCommitsByProject(Long id);

    /**
     * Method getting commit by identifier
     * @param projectId     Project ID
     * @param identifier    Identifier of commit
     * @return              Commit with that identifier
     */
    Commit getCommitByIdentifier(Long projectId, String identifier);
}
