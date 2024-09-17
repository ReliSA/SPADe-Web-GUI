package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IterationsAndPhasesService {

    public ResponseEntity<String> getIterationAndPhases(Project project);

    public ResponseEntity<Map<String, Object>> changeToPhase(String[] iterationsIds);

    public ResponseEntity<Map<String, Object>> changeToIteration(String[] phasesIds);
}
