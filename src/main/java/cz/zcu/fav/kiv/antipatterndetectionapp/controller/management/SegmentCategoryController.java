package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * This class contains all endpoints of segment-category.html
 */
@Controller
public class SegmentCategoryController {

    private final Logger LOGGER = LoggerFactory.getLogger(SegmentCategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IterationService iterationService;

    @Autowired
    private PhaseService phaseService;

//    //@Autowired
    //private ActivityService activityService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectInstanceService projectInstanceService;

    @Autowired
    private WorkUnitService workUnitService;

    /**
     * Method for showing category page for specific project
     * @param model         Object for passing data to the UI
     * @param project       Selected project for showing categories
     * @param redirectAttrs Attributes for redirection
     * @return              HTML template
     */
    @GetMapping("/management/segment-category")
    public String category(Model model,
                                @RequestParam(value = "selectedProject", required = false) Long project,
                                RedirectAttributes redirectAttrs,
                                HttpSession session) {

        // First open in session
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/segment-category\") - Accessing page");
            return "management/segment-category";
        }

        // Project selected
        if(project != null) {
            session.setAttribute("project", project);
            Utils.sessionRecreate(session);
        }

        project = (long) session.getAttribute("project");

        Project foundedProject = projectService.getProjectById(project);
        if(foundedProject == null) {
            if(!Objects.equals(project, Constants.DEFAULT_ID)) {
                redirectAttrs.addFlashAttribute("errorMessage", "Project not found");
                LOGGER.info("@GetMapping(\"/management/segment-category/\") - Project not found");
            } else {
                LOGGER.info("@GetMapping(\"/management/segment-category/\") - Accessing page");
            }
            model.addAttribute("projects", projectService.getAllProjects());
            return "management/segment-category";
        }

        //Add all categories into model
        List<Category> categories = new ArrayList<>();
        for(ProjectInstance projectInstance : foundedProject.getProjectInstances()) {
            categories.addAll(projectInstance.getCategories());
        }
        Collections.sort(categories, Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("categories", categories);
        model.addAttribute("projects", projectService.getAllProjects());
        LOGGER.info("@GetMapping(\"/management/segment-category/\") - Accessing page");

        return "/management/segment-category";
    }

    /**
     * Method preparing selected categories for transformation into defined type
     * @param model                 Object for passing data to the UI
     * @param selectedCategories    List of selected categories
     * @param submitType            Type of required attribute (1 - Iteration, 2 - Phase, 3 - Activity)
     * @param submitId              String with type of required attribute and id of one selected category
     * @param redirectAttrs         Attributes for redirection
     * @return                      HTML template
     */
    @PostMapping(value = "/categoryChange")
    public String categoryChange(Model model,
                                 @RequestParam(value = "selectedBox", required = false) List<Category> selectedCategories,
                                 @RequestParam(value = "submitType", required = false) Integer submitType,
                                 @RequestParam(value = "submitId", required = false) String submitId,
                                 RedirectAttributes redirectAttrs) {

        if(selectedCategories != null && submitType != null) { // Selected submit

            if(submitType <= 0 || submitType > Constants.SUBMIT_TYPES) { // ERROR: Unknown submitType
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Application error occurred");
                LOGGER.warn("@PostMapping(\"/management/segment-category\") - Cannot proceed: Unknown submitType " + submitType);
                return "redirect:/management/segment-category";
            }

        } else if(submitId != null) { //Inline submit

            Pair<Integer, Long> resultPair = extractTypeAndId(submitId);
            if(resultPair == null) { // Cannot be extracted
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Application error occurred");
                LOGGER.warn("@PostMapping(\"/management/segment-category\") - Cannot proceed: Unknown submitId " + submitId);
                return "redirect:/management/segment-category";
            }

            // Add one category into new list
            submitType = resultPair.getFirst();
            Long id = resultPair.getSecond();
            selectedCategories = new ArrayList<>();
            selectedCategories.add(categoryService.getCategoryById(id));

        } else { //Unspecific error
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected before submission");
            LOGGER.warn("@PostMapping(\"/management/segment-category\") - Cannot proceed: Submission went wrong");
            return "redirect:/management/segment-category";
        }

        // Process all selected categories
        Pair<String, Boolean> result = processSelectedCategories(submitType, selectedCategories);
        if(!result.getSecond()) {
            redirectAttrs.addFlashAttribute("successMessage", "All selected categories (" + selectedCategories.size() + ") were assign");
        } else {
            redirectAttrs.addFlashAttribute("informMessage", "Some categories cannot be assign, look at the log for more information");
        }
        redirectAttrs.addFlashAttribute("message", result.getFirst());

        return "redirect:/management/segment-category";
    }

    /**
     * Method for transforming selected categories into defined attribute type
     * @param submitType            Type of required attribute (1 - Iteration, 2 - Phase, 3 - Activity)
     * @param selectedCategories    Selected categories for transformation
     * @return                      Log message about transformation
     */
    private Pair<String, Boolean> processSelectedCategories(Integer submitType, List<Category> selectedCategories) {

        boolean errorOccurred = false;
        StringBuilder message = new StringBuilder();
        for(Category category : selectedCategories) {
            message.append("Category: ").append(category.getName()).append(System.getProperty("line.separator"));

            if(category.getWorkUnits().size() == 0) { //Inform user that there aren't any WorkUnits
                errorOccurred = true;
                message.append(Constants.INDENT)
                        .append("cannot be assigned due to non-existent work unit")
                        .append(System.getProperty("line.separator"))
                        .append(System.getProperty("line.separator"));
                continue;
            }

            DatabaseObject object = getNewObject(category.getWorkUnits(), submitType, category);

            for(WorkUnit workUnit : category.getWorkUnits()) {

                boolean hasAttr = true;
                switch (submitType) {
                    case 1:
                        if(workUnit.getIteration() == null) {
                            hasAttr = false;
                            workUnit.setIteration((Iteration) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                    case 2:
                        if(workUnit.getPhase() == null) {
                            hasAttr = false;
                            workUnit.setPhase((Phase) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                    case 3:
                        if(workUnit.getActivity() == null) {
                            hasAttr = false;
                            workUnit.setActivity((Activity) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                }

                if(!hasAttr) { // Transformation of selected category was done

                    message.append(Constants.INDENT)
                            .append("successfully assigned to WU with id ").append(workUnit.getId())
                            .append(System.getProperty("line.separator"));

                } else { // WorkUnit already has iteration, inform user

                    errorOccurred = true;
                    message.append(Constants.INDENT)
                            .append("cannot be assigned to WU with id ").append(workUnit.getId())
                            .append(" as it already exists with ").append(workUnit.getIteration().getName())
                            .append(System.getProperty("line.separator"));
                }
            }
            message.append(System.getProperty("line.separator"));
        }
        return Pair.of(message.toString(), errorOccurred);
    }

    /**
     * Method for checking, if any work units in set has empty attribute of submitType
     * If any of them is empty, method create and return that new attribute type
     * @param workUnits     Set of work units, where test will be done
     * @param submitType    Type of required attribute (1 - Iteration, 2 - Phase, 3 - Activity)
     * @param category      Category that should be transformed into the new attribute
     * @return              New attribute for transformation of category, if there is at least one WU with empty attribute
     *                      or NULL if there aren't any empty attribute
     */
    private DatabaseObject getNewObject(Set<WorkUnit> workUnits, Integer submitType, Category category) {

        switch (submitType) {
            case 1:
                for(WorkUnit workUnit : workUnits) {
                    if(workUnit.getIteration() == null) {
                        Iteration iteration = new Iteration(category.getExternalId(),
                                                category.getName(),
                                                category.getDescription(),
                                                category.getProjectInstance().getProjectId());
                        return iterationService.saveIteration(iteration);
                    }
                }
                break;
            case 2:
                for(WorkUnit workUnit : workUnits) {
                    if(workUnit.getPhase() == null) {
                        Phase phase = new Phase(category.getExternalId(),
                                    category.getName(),
                                    category.getDescription(),
                                    category.getProjectInstance().getProjectId());
                        return phaseService.savePhase(phase);
                    }
                }
                break;
            case 3:
                for(WorkUnit workUnit : workUnits) {
                    if(workUnit.getActivity() == null) {
                        Activity activity = new Activity(category.getExternalId(),
                                    category.getName(),
                                    category.getDescription(),
                                    category.getProjectInstance().getProjectId());
//                        return activityService.saveActivity(activity);
                    }
                }
                break;
        }
        return null;
    }

    /**
     * Method for parsing type of submission and id of category
     * @param submitId  String with type and category id
     * @return          Pair of type and id
     */
    private Pair<Integer, Long> extractTypeAndId(String submitId) {
        String[] parts = submitId.split(Constants.HTML_DELIMITER);
        //Project ahoj = Project.builder().projectName("").projectName2("das").build();

        try {
            Integer type = Integer.parseInt(parts[0]);
            Long id = Long.parseLong(parts[1]);
            return Pair.of(type, id);

        } catch (NumberFormatException e) {
            return null;
        }

    }
}