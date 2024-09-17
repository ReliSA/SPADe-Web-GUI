package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT * FROM project WHERE superProjectId IS NULL", nativeQuery = true)
    List<Project> getParentProjects();

    @Query(value = "SELECT * FROM project WHERE superProjectId = :superProjectId", nativeQuery = true)
    List<Project> getSubordinateProjectsTo(Long superProjectId);
}
