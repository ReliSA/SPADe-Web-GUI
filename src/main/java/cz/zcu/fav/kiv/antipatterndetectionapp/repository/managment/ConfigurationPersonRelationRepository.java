package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ConfigurationPersonRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfigurationPersonRelationRepository extends JpaRepository<ConfigurationPersonRelation, Long> {

    @Query(value = "SELECT * FROM configuration_person_relation INNER JOIN person ON configuration_person_relation.personId = person.id WHERE configuration_person_relation.configurationId = 1 AND configuration_person_relation.name = 'Committed-by'", nativeQuery = true)
    ConfigurationPersonRelation getCommittedRelation(Long projectId);
}
