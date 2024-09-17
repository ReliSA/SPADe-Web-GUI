package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types.Node;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;


/**
 * This class contains all endpoints connected to Projects
 */
@Controller
@RequestMapping("v2/management")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projectsList")
    public ResponseEntity<String> getProjectsList() {
        ProjectToDto projectToDto = new ProjectToDto();
        List<ProjectDto> projects = projectToDto.convert(projectService.getAllProjects());
        return ResponseEntity.status(projects.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(new Gson().toJson(projects));
    }

    @GetMapping("/projectsHierarchy")
    public ResponseEntity<String> getProjectsHierarchy() {
        ProjectToDto projectToDto = new ProjectToDto();
        List<Node> parents = new ArrayList<>();
        List<ProjectDto> parentProjects = projectToDto.convert(projectService.getParentProjects());
        for(ProjectDto parentProject : parentProjects) {
            Node n = new Node();
            n.project = parentProject;
            n.children = new ArrayList<>();
            parents.add(n);
        }

        for(Node n : parents) {
            n.children = projectService.calculate(n.project);
        }

        return ResponseEntity.status(parents.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(new Gson().toJson(parents));
    }

    @PostMapping("/saveProjects")
    public ResponseEntity<String> saveProjects(@RequestBody Map<String, Object> requestData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String nodesString = objectMapper.writeValueAsString(requestData.get("projects"));
        Node[] nodes = objectMapper.readValue(nodesString, Node[].class);

        Map<String, Object> json = new HashMap<>();

        if(projectService.saveProjectsStructure(Arrays.asList(nodes), null)) {
            json.put("message", "Project data saved successfully.");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.OK);
        }
        else {
            json.put("message", "Project data save failed.");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}

