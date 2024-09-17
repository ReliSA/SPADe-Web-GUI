package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit;

import com.sun.istack.NotNull;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface WorkUnitServiceV2 {
    List<WorkUnit> fetchProjectWorkUnits(long activityId, String category, List <String> type);
    boolean updateWorkUnitsActivity(long activityId, List<Long> wuIds);

    List<String> fetchProjectWorkUnitCategories(Long projectId);
    List<String> fetchProjectWorkUnitTypes(Long projectId);



}
