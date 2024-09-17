package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PersonService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonMergeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PersonToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class contains all endpoints of management/persons
 */
@Controller
@RequestMapping("v2/management")
public class PersonController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PersonService personService;

    @PostMapping("/personsFromProject")
    public ResponseEntity<String> getPersonsFromProject(@RequestBody Map<String, String> requestData) {
        long ProjectId = Long.parseLong(requestData.get("projectId").toString());
        ObjectMapper objectMapper = new ObjectMapper();

        Project project = projectService.getProjectById(ProjectId);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }
        List<Person> persons = project.getPeople();
        List<PersonDto> personDtos = new PersonToDto().convert(persons);
        Collections.sort(personDtos, Comparator.comparing(PersonDto::getName, String.CASE_INSENSITIVE_ORDER));

        try {
            String json = objectMapper.writeValueAsString(personDtos);
            return ResponseEntity.status(persons != null ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to serialize data to JSON");
        }
    }

    @PostMapping("/mergePersons")
    public ResponseEntity<String> mergeToSelected(@RequestBody PersonMergeRequest mergeRequest) {
        long projectId = mergeRequest.getProjectId();
        Project personProject = projectService.getProjectById(projectId);

        List<PersonDto> personsToMerge = mergeRequest.getPersons();
        PersonDto personToMergeIn = mergeRequest.getPerson();
        String newName = mergeRequest.getNewPersonName();

        if (personService.mergePersons(personProject, personsToMerge, personToMergeIn, newName)) {
            return ResponseEntity.status(HttpStatus.OK).body("Persons successfully merged");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Persons merge failed");
    }
}

