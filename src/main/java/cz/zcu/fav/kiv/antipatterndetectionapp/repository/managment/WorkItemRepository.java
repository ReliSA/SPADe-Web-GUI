package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface WorkItemRepository extends JpaRepository<WorkItem, Long> {
}
