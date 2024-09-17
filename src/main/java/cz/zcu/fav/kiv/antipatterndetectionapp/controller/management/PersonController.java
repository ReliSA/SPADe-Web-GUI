package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This class contains all endpoints of person.html
 */
//@Controller
public class PersonController {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private ProjectService projectService;

    /**
     * Method for showing people and their identities
     * @param model         Object for passing data to the UI
     * @param project       Selected project for showing people
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    //[JT] vyhodit http session
    @GetMapping("/management/person")
    public String people(Model model,
                         @RequestParam(value = "selectedProject", required = false) Long project,
                         RedirectAttributes redirectAttrs,
                         HttpSession session) {

        // [JT] tohle taky hodit pryc
        //session pro nas neni platna - posilat v body requestu z klienta
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/person\") - Accessing page");
            //[JT] prepsat do reactu
            return "management/person";
        }
        //[JT] tohle pryc
        // Project selected
        if(project != null) {
            session.setAttribute("project", project);
            Utils.sessionRecreate(session);
        }
        //[JT] nerozumim
        project = (long) session.getAttribute("project");

        Project foundedProject = projectService.getProjectById(project);
        //[JT] logika do service
        if(foundedProject == null) {
            if(!Objects.equals(project, Constants.DEFAULT_ID)) {
                redirectAttrs.addFlashAttribute("errorMessage", "Project not found");
                LOGGER.info("@GetMapping(\"/management/person\")- Project not found");
            } else {
                LOGGER.info("@GetMapping(\"/management/person\") - Accessing page");
            }
            //[JT] do reactu
            model.addAttribute("projects", projectService.getAllProjects());
            return "management/person";
        }

        List<Person> people = foundedProject.getPeople();
        Collections.sort(people, Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER));
        //[JT] hodit do json response body
        model.addAttribute("people", people);
        model.addAttribute("projects", projectService.getAllProjects());
        LOGGER.info("@GetMapping(\"/management/person\") - Accessing page");

        return "management/person";
    }

    /**
     * Method for merging people in same project
     * @param model                 Object for passing data to the UI
     * @param selectedPeople        People selected by user for merge
     * @param personName            Name of the new person for merge
     * @param redirectAttrs         Attributes for redirection
     * @return                      HTML template
     */
    @PostMapping(value = "/changeIdentities")
    public String mergeIdentities(Model model,
                                  @RequestParam(value = "selectedBox", required = false) List<Person> selectedPeople,
                                  @RequestParam(value = "personName", required = false) List<String> personName,
                                  @RequestParam(value = "radioBtn", required = false) Integer radioBtn,
                                  @RequestParam(value = "submitId", required = false) Long submitId,
                                  RedirectAttributes redirectAttrs) {

        // Nothing was selected
        if(selectedPeople == null || selectedPeople.isEmpty()) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected");
            LOGGER.info("@PostMapping(\"/management/person\") - Nothing was selected or name was empty");

        } else {

            Person newPerson;
            if(Objects.equals(submitId, Constants.DEFAULT_ID)) { // Creating new person

                if(personName == null || personName.isEmpty() || radioBtn == null || personName.get(radioBtn).isEmpty()) { // Person name is not set
                    redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Name of the new person is empty");
                    LOGGER.info("@PostMapping(\"/management/person\") - Name of the new person is empty");
                    return "redirect:/management/person";
                }

                Person createdPerson = new Person(personName.get(radioBtn), selectedPeople.get(0).getProject());
                newPerson = personService.savePerson(createdPerson);

            } else { // Merging to existing person
                newPerson = personService.getPersonById(submitId);
            }

            if(newPerson != null) { // Person was found in database

                if(selectedPeople.contains(newPerson)) {
                    redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Person cannot be merged with itself");
                    LOGGER.info("@PostMapping(\"/management/person\") - Person cannot be merged with itself");
                    return "redirect:/management/person";
                }

                for(Person person : selectedPeople) { // Relation update for every selected person
                    personService.updatePersonRelations(person.getId(), newPerson.getId());
                    personService.deletePerson(person.getId());
                }

                redirectAttrs.addFlashAttribute("successMessage", "All identities of selected person were merged to person " + newPerson.getName());
                LOGGER.info("@PostMapping(\"/management/person\") - Selected identities were merged to person with id " + submitId);

            } else {
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Selected person was not found");
                LOGGER.info("@PostMapping(\"/management/person\") - Selected person with id " + submitId + " was not found");
            }

        }
        return "redirect:/management/person";
    }
}
