package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface RelationRepository extends JpaRepository<Relation, Long> {
}
