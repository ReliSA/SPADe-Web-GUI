package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.detection;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserDetectionDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DetectionService {
    Query getAllProjectsAndAntipatterns();
    List<QueryResult> analyze(UserDetectionDto detectionRequest);

}
