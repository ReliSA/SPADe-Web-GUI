package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface IterationRepository extends JpaRepository<Iteration, Long> {

    @Query("select it from Iteration it where it.id IN :ids")
    List<Iteration> fetchIterationsByIds(List<Long> ids);

}
