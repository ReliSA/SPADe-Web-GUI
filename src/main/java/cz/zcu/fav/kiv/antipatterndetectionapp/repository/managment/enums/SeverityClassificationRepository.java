package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.SeverityClassification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface SeverityClassificationRepository extends JpaRepository<SeverityClassification, Long> {
}
