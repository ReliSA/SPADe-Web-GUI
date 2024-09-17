package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.ActivityRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.CategoryService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums.WuTypeService;
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
import java.util.*;

/**
 * This class contains all endpoints of segment-activity.html
 */
@Controller
public class SegmentActivityController {

    private final Logger LOGGER = LoggerFactory.getLogger(SegmentActivityController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkUnitService workUnitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WuTypeService wuTypeService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private WorkUnitRepository workUnitRepository;

    /**
     * Method for visualization of activities and work units
     * @param model         Object for passing data to the UI
     * @param project       Selected project for showing workunits and activities
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @GetMapping("/management/segment-activity")
    public String changeProject(Model model,
                                @RequestParam(value = "selectedProject", required = false) Long project,
                                RedirectAttributes redirectAttrs,
                                HttpSession session) {

        // Remove activity if different project selected
        if(project != null && session.getAttribute("project") != project) {
            Utils.sessionRecreate(session);
        }

        // First open in session
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/segment-activity\") - Accessing page");
            return "management/segment-activity";
        }

        // Project selected
        if(project != null) {
            session.setAttribute("project", project);
        }

        project = (long) session.getAttribute("project");

        Project foundedProject = projectService.getProjectById(project);

        if(foundedProject == null) {
            if(!Objects.equals(project, Constants.DEFAULT_ID)) {
                redirectAttrs.addFlashAttribute("errorMessage", "Project not found");
                LOGGER.info("@GetMapping(\"/management/segment-activity\")- Project not found");
            } else {
                session.removeAttribute("activity");
                LOGGER.info("@GetMapping(\"/management/segment-activity\") - Accessing page");
            }
            model.addAttribute("projects", projectService.getAllProjects());
            return "management/segment-activity";
        }

        applyFilter(model, foundedProject, session);

        model.addAttribute("activities", foundedProject.getActivities());
        model.addAttribute("projects", projectService.getAllProjects());
        return "management/segment-activity";
    }

    /**
     * Method for selection of activity
     * @param model         Object for passing data to the UI
     * @param activityId    Selected activity for assigning
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping("/selectActivity")
    public String selectActivity(Model model,
                                 @RequestParam(value = "submitId", required = false) Long activityId,
                                 RedirectAttributes redirectAttrs,
                                 HttpSession session) {

//        Activity activity = activityService.getActivityById(activityId);
//        if(activity == null) {
//            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Activity not found");
//            LOGGER.info("@GetMapping(\"/management/segment-activity\")- Activity not found");
//            return "redirect:/management/segment-activity";
//        }
//
//        session.setAttribute("activity", activity);
//        LOGGER.info("@GetMapping(\"/management/segment-activity\") - Accessing page");
//        return "redirect:/management/segment-activity";
        return null;
    }

    /**
     * Method for reselection of activity (removing activity from session)
     * @param session   Session with attributes
     * @return          HTML template
     */
    @PostMapping("/reselectActivity")
    public String reselectActivity(HttpSession session) {
        session.removeAttribute("activity");
        return "redirect:/management/segment-activity";
    }

    /**
     * Method adding category to the filter for current work units
     * @param model         Object for passing data to the UI
     * @param categoryId    Identifier of type that will be added
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping("/addCategoryFilter")
    public String addCategory(Model model,
                              @RequestParam(value = "selectedCategory", required = false) Long categoryId,
                              HttpSession session) {

        List<Category> categoryFilter = (List<Category>) session.getAttribute("categoryFilter");
        if(categoryFilter == null) {
            categoryFilter = new ArrayList<>();
        }

        Category category = categoryService.getCategoryById(categoryId);
        if(category != null) {
            categoryFilter.add(category);
        }
        session.setAttribute("categoryFilter", categoryFilter);
        return "redirect:/management/segment-activity";
    }

    /**
     * Method removing category from filter for current work units
     * @param model         Object for passing data to the UI
     * @param categoryId    Identifier of category that will be removed
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping("/removeCategoryFilter")
    public String removeCategory(Model model,
                                 @RequestParam(value = "selectedCategory", required = false) Long categoryId,
                                 HttpSession session) {


        List<Category> categoryFilter = (List<Category>) session.getAttribute("categoryFilter");
        if(categoryFilter == null) {
            return "redirect:/management/segment-activity";
        }

        Category category = categoryService.getCategoryById(categoryId);
        if(category != null) {
            categoryFilter.remove(category);
        }
        session.setAttribute("categoryFilter", categoryFilter);
        return "redirect:/management/segment-activity";
    }

    /**
     * Method adding type to the filter for current work units
     * @param model     Object for passing data to the UI
     * @param typeId    Identifier of type that will be added
     * @param session   Session with attributes
     * @return          HTML template
     */
    @PostMapping("/addTypeFilter")
    public String addType(Model model,
                          @RequestParam(value = "selectedType", required = false) Long typeId,
                          HttpSession session) {

        List<WuType> typeFilter = (List<WuType>) session.getAttribute("typeFilter");
        if(typeFilter == null) {
            typeFilter = new ArrayList<>();
        }

        WuType wuType = wuTypeService.getWuTypeById(typeId);
        if(wuType != null) {
            typeFilter.add(wuType);
        }
        session.setAttribute("typeFilter", typeFilter);
        return "redirect:/management/segment-activity";
    }

    /**
     * Method removing type from filter for current work units
     * @param model     Object for passing data to the UI
     * @param typeId    Identifier of type that will be removed
     * @param session   Session with attributes
     * @return          HTML template
     */
    @PostMapping("/removeTypeFilter")
    public String removeType(Model model,
                             @RequestParam(value = "selectedType", required = false) Long typeId,
                             HttpSession session) {

        List<WuType> typeFilter = (List<WuType>) session.getAttribute("typeFilter");
        if(typeFilter == null) {
            return "redirect:/management/segment-activity";
        }

        WuType wuType = wuTypeService.getWuTypeById(typeId);
        if(wuType != null) {
            typeFilter.remove(wuType);
        }
        session.setAttribute("typeFilter", typeFilter);
        return "redirect:/management/segment-activity";
    }

    /**
     * Method assigning activity to selected work units
     * @param model         Object for passing data to the UI
     * @param selectedUnits Selected work units
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping("/assignActivity")
    public String assignActivity(Model model,
                                 @RequestParam(value = "selectedUnits", required = false) List<WorkUnit> selectedUnits,
                                 RedirectAttributes redirectAttrs,
                                 HttpSession session) {

        if(selectedUnits == null || selectedUnits.isEmpty()) {
            selectedUnits = (List<WorkUnit>) session.getAttribute("selectedWU");
            session.removeAttribute("selectedWU");
            if(selectedUnits == null || selectedUnits.isEmpty()) {
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected");
                LOGGER.info("@PostMapping(\"/management/segment-activity\" - Nothing was selected");
                return "redirect:/management/segment-activity";
            }
        }

        Activity activity = (Activity) session.getAttribute("activity");
        if(activity != null) {

            for(WorkUnit workUnit : selectedUnits) {
                workUnit.setActivity(activity);
                workUnitService.saveWorkUnit(workUnit);
            }

        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Error occurred while getting selected activity");
            LOGGER.info("@PostMapping(\"/management/segment-activity\" - Session has problem with activity extract");
            return "redirect:/management/segment-activity";
        }

        redirectAttrs.addFlashAttribute("successMessage", "Activity was assigned to all selected work units (" + selectedUnits.size() + ")");
        LOGGER.info("@PostMapping(\"/management/segment-activity\" - Activity was assigned to all selected work units");
        return "redirect:/management/segment-activity";
    }

    /**
     * Method testing if there are some work units with activities
     * @param model         Object for passing data to the UI
     * @param selectedUnits Selected work units
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping("/testWU")
    public String testWU(Model model,
                         @RequestParam(value = "selectedUnits", required = false) List<WorkUnit> selectedUnits,
                         RedirectAttributes redirectAttrs,
                         HttpSession session) {

        if(selectedUnits == null || selectedUnits.isEmpty()) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected");
            LOGGER.info("@PostMapping(\"/management/segment-activity\" - Nothing was selected");
            return "redirect:/management/segment-activity";
        }

        List<Long> ids = new ArrayList<>();
        boolean notEmpty = false;
        for(WorkUnit workUnit : selectedUnits) {
            ids.add(workUnit.getId());
            if(workUnit.getActivity() != null) {
                notEmpty = true;
            }
        }

        if(notEmpty) {
            session.setAttribute("selectedWU", selectedUnits);
            redirectAttrs.addFlashAttribute("selectedWU", selectedUnits);
            redirectAttrs.addFlashAttribute("selectedIds", ids);
            return "redirect:/management/segment-activity";
        }

        return assignActivity(model, selectedUnits, redirectAttrs, session);
    }

    /**
     * Method for applying filters defined by user into model
     * @param model             Object for passing data to the UI
     * @param foundedProject    Selected project for showing template
     * @param session           Session with attributes
     */
    private void applyFilter(Model model,
                             Project foundedProject,
                             HttpSession session) {

        Set<Category> categories = new HashSet<>();
        Set<WuType> types = new HashSet<>();
        for(ProjectInstance instance : foundedProject.getProjectInstances()) {
            categories.addAll(instance.getCategories());
            types.addAll(instance.getWuTypes());
        }

        //Remove categories in filter
        List<Category> categoryList = new ArrayList<>(categories);
        List<Category> categoryFilter = (List<Category>) session.getAttribute("categoryFilter");
        if(categoryFilter != null) {
            categoryFilter.forEach(categoryList::remove);
        }

        List<WuType> wuTypeList = new ArrayList<>(types);
        List<WuType> wuTypeFilter = (List<WuType>) session.getAttribute("typeFilter");
        if(wuTypeFilter != null) {
            wuTypeFilter.forEach(wuTypeList::remove);
        }

        //Sort categories and types
        Collections.sort(categoryList, Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER));
        Collections.sort(wuTypeList, Comparator.comparing(WuType::getName, String.CASE_INSENSITIVE_ORDER));

        List<Long> categoryFilterId = new ArrayList<>();
        List<Long> wuTypeFilterId = new ArrayList<>();

        if(categoryFilter != null) {
            for(Category category : categoryFilter) {
                categoryFilterId.add(category.getId());
            }
        }

        if(wuTypeFilter != null) {
            for(WuType type : wuTypeFilter) {
                wuTypeFilterId.add(type.getId());
            }
        }

        if(session.getAttribute("categoryFilter") != null && !categoryFilter.isEmpty() && session.getAttribute("typeFilter") != null && !wuTypeFilter.isEmpty()) {
            model.addAttribute("workunits", workUnitRepository.getFiltredWorkUnits(foundedProject.getId(), categoryFilterId, wuTypeFilterId, categoryFilterId.size()));
        } else if(session.getAttribute("categoryFilter") != null && !categoryFilter.isEmpty()) {
            model.addAttribute("workunits", workUnitRepository.getCategoryFiltredWorkUnits(foundedProject.getId(), categoryFilterId, categoryFilterId.size()));
        } else if(session.getAttribute("typeFilter") != null && !wuTypeFilter.isEmpty()) {
            model.addAttribute("workunits", workUnitRepository.getTypeFiltredWorkUnits(foundedProject.getId(), wuTypeFilterId));
        } else {
            model.addAttribute("workunits", foundedProject.getWorkUnits());
        }

        model.addAttribute("categories", categoryList);
        model.addAttribute("types", wuTypeList);
    }
}
