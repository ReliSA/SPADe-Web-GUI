package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE work_unit SET assigneeId = :newPersonId WHERE assigneeId = :personId", nativeQuery = true)
    void updatePersonUnits(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE work_item SET authorId = :newPersonId WHERE authorId = :personId", nativeQuery = true)
    void updatePersonItems(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE configuration_person_relation SET personId = :newPersonId WHERE personId = :personId", nativeQuery = true)
    void updatePersonConfigurationRelation(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE person_role SET personId = :newPersonId WHERE personId = :personId", nativeQuery = true)
    void updatePersonRole(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE identity SET personId = :newPersonId WHERE personId = :personId", nativeQuery = true)
    void updatePersonIdentities(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE person_competency SET personId = :newPersonId WHERE personId = :personId", nativeQuery = true)
    void updatePersonCompetencies(Long personId, Long newPersonId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE group_member SET memberId = :newPersonId WHERE memberId = :personId", nativeQuery = true)
    void updatePersonGroups(Long personId, Long newPersonId);
}
