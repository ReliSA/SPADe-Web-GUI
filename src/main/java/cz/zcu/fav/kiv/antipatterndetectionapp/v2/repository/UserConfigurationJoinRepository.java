package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigurationJoin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * this repository is not being used (29.04.2023)
 * but it provides and API to work with Join table user_configurations
 * the logic is contained in ConfigurationRepository interface (the pairing of user and configuration)
 */
public interface UserConfigurationJoinRepository extends JpaRepository<UserConfigurationJoin, UserConfigKey> {
}
