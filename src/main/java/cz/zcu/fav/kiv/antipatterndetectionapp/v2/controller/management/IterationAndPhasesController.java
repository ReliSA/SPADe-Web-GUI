package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.managment.IterationsAndPhasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * This class contains all endpoints of management/persons
 */
@Controller
@RequestMapping("v2/management")
public class IterationAndPhasesController {

    @Autowired
    private IterationsAndPhasesService iterationsAndPhasesService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/segment-iteration-phase")
    public ResponseEntity<String> getIterationAndPhasesFromProject(@RequestParam Map<String, String> requestData) {
        long ProjectId = Long.parseLong(requestData.get("projectId").toString());

        Project project = projectService.getProjectById(ProjectId);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Project not found.");
        }

        return iterationsAndPhasesService.getIterationAndPhases(project);
    }

    @PostMapping("/changeIteration")
    public ResponseEntity<Map<String, Object>> changeIteration(@RequestBody Map<String, String[]> requestData) {
        if (requestData.get("Ids").length == 0) {
            Map<String,Object> json = new HashMap<>();
            json.put("errormessage", "No Iterations send.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        }

        return iterationsAndPhasesService.changeToPhase(requestData.get("Ids"));
    }

    @PostMapping("/changePhase")
    public ResponseEntity<Map<String, Object>> changePhase(@RequestBody Map<String, String[]> requestData) {
        if (requestData.get("Ids").length == 0) {
            Map<String,Object> json = new HashMap<>();
            json.put("errormessage", "No Phases send.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        }

        return iterationsAndPhasesService.changeToIteration(requestData.get("Ids"));
    }

}
