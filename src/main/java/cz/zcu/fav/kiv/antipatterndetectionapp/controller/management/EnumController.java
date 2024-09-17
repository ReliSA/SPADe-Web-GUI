package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.FormObject;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.Service;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * This class contains all endpoints of enums.html
 */
@Controller
public class EnumController {

    private final Logger LOGGER = LoggerFactory.getLogger(EnumController.class);

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private SeverityService severityService;

    @Autowired
    private RelationService  relationService;

    @Autowired
    private ResolutionService resolutionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private WuTypeService wuTypeService;

    @Autowired
    private PriorityClassificationService priorityClassificationService;

    @Autowired
    private SeverityClassificationService severityClassificationService;

    @Autowired
    private StatusClassificationService statusClassificationService;

    @Autowired
    private RoleClassificationService roleClassificationService;

    @Autowired
    private ResolutionClassificationService resolutionClassificationService;

    @Autowired
    private WuTypeClassificationService wuTypeClassificationService;

    @Autowired
    private RelationClassificationService relationClassificationService;

    @Autowired
    private ProjectService projectService;

    private Map<String, Pair<Service, EnumService>> services;

    /**
     * Method initializing map of enum
     * services and their classifications
     */
    @PostConstruct
    private void initializeMap() {
        services = Map.ofEntries(
                Map.entry("Priority", Pair.of(priorityClassificationService, priorityService)),
                Map.entry("Severity", Pair.of(severityClassificationService, severityService)),
                Map.entry("Status", Pair.of(statusClassificationService, statusService)),
                Map.entry("Role", Pair.of(roleClassificationService, roleService)),
                Map.entry("Resolution", Pair.of(resolutionClassificationService, resolutionService)),
                Map.entry("WuType", Pair.of(wuTypeClassificationService, wuTypeService)),
                Map.entry("Relation", Pair.of(relationClassificationService, relationService))
        );
    }

    /**
     * Method for showing enums in tabs
     * @param model         Object for passing data to the UI
     * @param project       Selected project for showing enums
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @GetMapping("/management/enums")
    public String changeTab(Model model,
                            @RequestParam(value = "selectedProject", required = false) Long project,
                            RedirectAttributes redirectAttrs,
                            HttpSession session) {

        // First open in session
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/enums\") - Accessing page");
            return "management/enums";
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
                LOGGER.info("@GetMapping(\"/management/enums\")- Project not found");
            } else {
                LOGGER.info("@GetMapping(\"/management/enums\") - Accessing page");
            }
            model.addAttribute("projects", projectService.getAllProjects());
            return "management/enums";
        }

        // For each project instance of project, get all enums
        for(Map.Entry<String, Pair<Service, EnumService>> entry : services.entrySet()) {

            Set<EnumType> enums = new HashSet<>(); // Enums of current enum type
            Service service = entry.getValue().getFirst(); // Service for current enum type

            // For each project instance, get all enums for current enum type
            for(ProjectInstance projectInstance : foundedProject.getProjectInstances()) {
                Set<EnumType> enumsByName = (Set<EnumType>) projectInstance.getEnumsByName(entry.getKey());
                if(enumsByName != null) {
                    enums.addAll(enumsByName);
                }
            }

            // Create classification pairs with enums
            List<Pair<Classification, List<EnumType>>> pairs = createClassPairs(service, enums);

            //Calculate rowspan for superclasses
            List<Integer> superClassSpan = calculateSpan(pairs);

            model.addAttribute(entry.getKey(), pairs);
            model.addAttribute(entry.getKey() + "All", service.getAllClasses());
            model.addAttribute(entry.getKey() + "Super", superClassSpan);
        }

        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("result", new FormObject());
        return "management/enums";
    }

    /**
     * Method for changing enum class for selected enums
     * @param model         Object for passing data to the UI
     * @param selectedEnums Selected enums for change of their class
     * @param selectedClass Selected class for enums
     * @param enumName      Name of the current selected tab
     * @param redirectAttrs Attributes for redirection
     * @return              Html template
     */
    @PostMapping(value = "/changeEnums")
    public String changeEnums(Model model,
                              @RequestParam(value = "selectedEnums", required = false) List<Long> selectedEnums,
                              @RequestParam(value = "selectedClass", required = false) Long selectedClass,
                              @RequestParam(value = "enumName", required = false) String enumName,
                              RedirectAttributes redirectAttrs) {

        if(selectedEnums == null || selectedEnums.isEmpty()) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected");
            LOGGER.info("@PostMapping(\"/management/enums\") - Nothing was selected");
            return "redirect:/management/enums";
        }

        for(Long id : selectedEnums) {
            Pair<Service, EnumService> pair = services.get(enumName);
            if(pair != null) {
                Service service = pair.getFirst();
                EnumService enumService = pair.getSecond();

                EnumType enumType = enumService.getEnumById(id);
                enumType.setClassId(service.getClassById(selectedClass));
                enumService.saveEnum(enumType);
            }
        }

        redirectAttrs.addFlashAttribute("successMessage", "All selected enums (" + selectedEnums.size() + ") were successfully assigned");
        redirectAttrs.addFlashAttribute("activeTab", enumName);
        return "redirect:/management/enums";
    }

    /**
     * Method creating pairs with enum type
     * classification and list of relevant enums
     * @param service   Enum service
     * @param enums     Set of enums in specific project
     * @return          List of pairs with enum class and relevant enums
     */
    private List<Pair<Classification, List<EnumType>>> createClassPairs(Service service, Set<EnumType> enums) {

        List<Pair<Classification, List<EnumType>>> pairs = new ArrayList<>(); // List with enum types classes and their enums
        for(Classification enumClass : service.getAllClasses()) {
            Set<EnumType> enumSet = new HashSet<>(); // For each enum classification create new list for enums

            //If enum class is equal to current class, add enum into list
            for(EnumType e : enums) {
                if(Objects.equals(e.getClassId().getId(), enumClass.getId())) {
                    enumSet.add(e);
                }
            }

            //If enum set has some enums in it, add pair to the final list
            if(!enumSet.isEmpty()) {
                List<EnumType> enumTypes = new ArrayList<>(enumSet);
                Collections.sort(enumTypes, Comparator.comparing(EnumType::getName, String.CASE_INSENSITIVE_ORDER));
                Pair<Classification, List<EnumType>> newPair = Pair.of(enumClass, enumTypes);
                pairs.add(newPair);
            }
        }
        return pairs;
    }

    /**
     * Method calculating rowspan for superclasses
     * @param pairs     Classification and list with enums
     * @return          List with sizes of rowspan for each classification
     */
    private List<Integer> calculateSpan(List<Pair<Classification, List<EnumType>>> pairs) {
        List<Integer> superClassSpan = new ArrayList<>(); // Superclass rowspan for current enum type
        String preSuperClass = null;
        int preListSize = 0;
        int lastI = 0;
        for(int i = 0; i < pairs.size(); i++) {
            Pair<Classification, List<EnumType>> currentPair = pairs.get(i);
            String currentSuperClass = currentPair.getFirst().getSuperClass();
            int currentListSize = currentPair.getSecond().size();
            if(preSuperClass == null || !currentSuperClass.contentEquals(preSuperClass)) {
                superClassSpan.add(i, currentListSize);
                lastI = i;
            } else if(currentSuperClass.contentEquals(preSuperClass)) {
                superClassSpan.set(lastI, preListSize + currentListSize);
                superClassSpan.add(i, 0);
                preListSize = preListSize + currentListSize;
                continue;
            }
            preSuperClass = currentSuperClass;
            preListSize = currentListSize;
        }

        return superClassSpan;
    }
}
