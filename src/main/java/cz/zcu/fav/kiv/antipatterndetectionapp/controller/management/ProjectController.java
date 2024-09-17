package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types.Node;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class contains all endpoints of projects.html
 */
//@Controller
public class ProjectController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    /**
     * Method for visualization of project hierarchy
     * @param model     Object for passing data to the UI
     * @return          HTML template
     */
    @GetMapping("/management/projects")
    public String projects(Model model) {
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

        model.addAttribute("parents", parents);
        LOGGER.info("@GetMapping(\"/management/projects\") - Accessing page");
        return "management/projects";
    }

    /**
     * Method for changing superproject of selected projects
     * @param model                 Object for passing data to the UI
     * @param selectedProjects      Selected projects for changing their parent project
     * @param selectedSuperProject  Selected project for being parent project
     * @param projectName           Name of new created project that will take a place as parent project
     * @param redirectAttrs         Redirect attributes
     * @return                      HTML template
     */
    @PostMapping(value = "/superProjectChange")
    public String projectChange(Model model,
                                @RequestParam(value = "selectedBox", required = false) List<Project> selectedProjects,
                                @RequestParam(value = "submitId", required = false) Long selectedSuperProject,
                                @RequestParam(value = "projectName", required = false) String projectName,
                                RedirectAttributes redirectAttrs) {

        // Validity check
        if(selectedProjects == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Nothing was selected before submission");
            LOGGER.warn("@PostMapping(\"/management/projects\") - Cannot proceed: Submission went wrong");

        } else if(projectService.getProjectById(selectedSuperProject) == null && !Objects.equals(selectedSuperProject, Constants.DEFAULT_ID)) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Superproject was not found");
            LOGGER.warn("@PostMapping(\"/management/projects\") - Cannot proceed: Submission went wrong");

        } else if(!Objects.equals(selectedSuperProject, Constants.DEFAULT_ID) && selectedProjects.contains(projectService.getProjectById(selectedSuperProject))) {
            if(selectedProjects.size() == 1) {
                selectedProjects.get(0).setSuperProject(null);
                projectService.saveProject(selectedProjects.get(0));
                redirectAttrs.addFlashAttribute("successMessage", "The project was set up as a parent project");
            } else {
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Project cannot be superproject for self");
                LOGGER.warn("@PostMapping(\"/management/projects\") - Cannot proceed: Submission went wrong");
            }

        } else {
            // User creating new superproject
            if(Objects.equals(selectedSuperProject, Constants.DEFAULT_ID)) {
                if(projectName == null || projectName.isEmpty()) {
                    redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Name was not found");
                    LOGGER.warn("@PostMapping(\"/management/projects\") - Cannot proceed: Submission went wrong");
                } else {
                    Project project = new Project(projectName);
                    Project newProject = projectService.saveProject(project);
                    for(Project subProject : selectedProjects) {
                        subProject.setSuperProject(newProject);
                        projectService.saveProject(subProject);
                    }
                    redirectAttrs.addFlashAttribute("successMessage", "Selected projects (" + selectedProjects.size() + ") were assign to " + newProject.getName());
                }
                return "redirect:/management/projects/";
            }

            // Check if there is no cycle
            List<Project> childProjects = new ArrayList<>();
            for(Project project : selectedProjects) {
                addChildProject(project, childProjects);
            }
            if(childProjects.contains(projectService.getProjectById(selectedSuperProject))) {
                redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Some selected projects cannot be assign - Selected superproject cannot be superior to its parent");
                LOGGER.warn("@PostMapping(\"/management/projects\") - Cannot proceed: Submission went wrong");
                return "redirect:/management/projects/";
            }

            // Changing superproject of all selected projects
            for(Project project : selectedProjects) {
                project.setSuperProject(projectService.getProjectById(selectedSuperProject));
                projectService.saveProject(project);
            }
            redirectAttrs.addFlashAttribute("successMessage", "Selected projects (" + selectedProjects.size() + ") were assign to " + projectService.getProjectById(selectedSuperProject).getName());
        }

        return "redirect:/management/projects/";
    }

    /**
     * Method for searching all subordinate projects of selected project
     * @param project       Project for which the search will be done
     * @param childProjects Collection of all subordinate projects
     */
    private void addChildProject(Project project, List<Project> childProjects) {
        for(Project childProject : projectService.getSubordinateProjectsTo(project.getId())) {
            childProjects.add(childProject);
            addChildProject(childProject, childProjects);
        }
    }

    /**
     * Method creating the hierarchy of projects by recursion
     * @param p Project for which the hierarchy will be generated
     * @return  List of project's children with their hierarchy
     */
    private ArrayList<Node> calculate(ProjectDto p) {
        ProjectToDto projectToDto = new ProjectToDto();
        ArrayList<Node> nodes = new ArrayList<>();
        List<ProjectDto> projects = projectToDto.convert(projectService.getSubordinateProjectsTo(p.getId()));
        for(ProjectDto project : projects) {
            Node n = new Node();
            n.project = project;
            n.children = calculate(project);
            nodes.add(n);
        }
        return nodes;
    }
}
