package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface CommitRepository extends JpaRepository<Commit, Long> {

//    @Query(value = "SELECT * FROM commit INNER JOIN configuration ON commit.id = configuration.id WHERE configuration.projectId = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM commit INNER JOIN configuration ON commit.id = configuration.id WHERE configuration.projectId = ?1 AND commit.isRelease = 1", nativeQuery = true)
    List<Commit> getReleasedCommitsByProject(Long projectId);

    @Query(value = "SELECT * FROM commit INNER JOIN configuration ON commit.id = configuration.id WHERE configuration.projectId = ?1 AND commit.identifier = ?2", nativeQuery = true)
    Commit getCommitByIdentifier(Long projectId, String identifier);
}
