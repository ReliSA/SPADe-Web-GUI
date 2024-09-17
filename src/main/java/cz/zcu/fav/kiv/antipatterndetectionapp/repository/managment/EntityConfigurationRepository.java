package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface EntityConfigurationRepository extends JpaRepository<Configuration, Long> {

}
