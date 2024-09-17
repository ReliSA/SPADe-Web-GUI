package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface WorkUnitRepository extends JpaRepository<WorkUnit, Long> {

    @Query(value = "SELECT * FROM work_unit " +
            "JOIN work_unit_category ON work_unit.id = work_unit_category.workUnitId " +
            "JOIN category ON category.id = work_unit_category.categoryId " +
            "WHERE category.id IN :categories " +
            "AND work_unit.wuTypeId IN :types " +
            "AND work_unit.projectId = :projectId " +
            "GROUP BY work_unit.id " +
            "HAVING COUNT(DISTINCT category.id) = :size", nativeQuery = true)
    List<WorkUnit> getFiltredWorkUnits(Long projectId, Collection<Long> categories, Collection<Long> types, Integer size);

    @Query(value = "SELECT DISTINCT * FROM work_unit " +
            "JOIN work_unit_category ON work_unit.id = work_unit_category.workUnitId " +
            "JOIN category ON category.id = work_unit_category.categoryId " +
            "WHERE category.id IN :categories " +
            "AND work_unit.projectId = :projectId " +
            "GROUP BY work_unit.id " +
            "HAVING COUNT(DISTINCT category.id) = :size", nativeQuery = true)
    List<WorkUnit> getCategoryFiltredWorkUnits(Long projectId, Collection<Long> categories, Integer size);

    @Query(value = "SELECT DISTINCT * FROM work_unit " +
            "WHERE work_unit.wuTypeId IN :types " +
            "AND work_unit.projectId = :projectId", nativeQuery = true)
    List<WorkUnit> getTypeFiltredWorkUnits(Long projectId, Collection<Long> types);
    @Query("SELECT unit FROM WorkUnit unit WHERE unit.activity.id = ?1 ")
    List<WorkUnit> fetchActivityWorkUnits(Long activityId);
    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = ?1")
    List<WorkUnit> fetchAllProjectWorkUnits(Long projectId);

    @Query("SELECT unit FROM WorkUnit unit " +
            "INNER JOIN Category cat " +
            "ON cat member OF unit.categories " +
            "WHERE unit.project.id = :projectId AND cat.name in :categoryName")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByCategory(@Param("projectId") Long projectId, @Param("categoryName") List<String> category);

    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = :projectId and unit.type.name in :typeNames")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByType(@Param("projectId") Long projectId, @Param("typeNames") List<String> wuTypes);
    @Query("SELECT unit from WorkUnit unit " +
            "INNER JOIN Category cat " +
            "ON cat MEMBER OF unit.categories " +
            "WHERE unit.project.id = :projectId AND cat.name in :categoryName AND unit.type.name in :typeNames")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByTypeAndCategory(@Param("projectId")Long projectId,@Param("categoryName") List<String> category,@Param("typeNames")  List <String> type);
    @Query("SELECT unit from WorkUnit unit ")
    List<String> fetchCategoryNames(Long projectId);
    @Query("UPDATE WorkUnit unit SET unit.activity.id = :activityId WHERE unit.id in :wuIds")
    @Modifying
    @Transactional
    int updateWuActivity(@Param("activityId")Long activityId, @Param("wuIds") List<Long> wuIds);
    @Query("SELECT DISTINCT cat.name from WorkUnit  unit INNER JOIN Category cat on cat MEMBER OF unit.categories AND unit.project.id = ?1")
    List<String> fetchProjectWorkUnitsCategories(Long projectId);
    @Query("SELECT DISTINCT unit.type.name from WorkUnit  unit WHERE unit.project.id = ?1")
    List<String> fetchProjectWorkUnitsTypes(Long projectId);


}
