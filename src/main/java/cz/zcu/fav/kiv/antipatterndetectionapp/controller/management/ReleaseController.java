package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Commit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.CommitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.ConfigurationPersonRelationRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.BranchService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.CommitService;
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

/**
 * This class contains all endpoints of release.html
 */
@Controller
public class ReleaseController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReleaseController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private ConfigurationPersonRelationRepository configurationPersonRelationRepository;

    /**
     * Method for visualization of released configurations
     * @param model     Object for passing data to the UI
     * @param project   Selected project for showing releases
     * @param session   Session with attributes
     * @return          HTML template
     */
    @GetMapping("/management/release")
    public String releases(Model model,
                           @RequestParam(value = "selectedProject", required = false) Long project,
                           HttpSession session) {

        // First open in session
        if(project == null && session.getAttribute("project") == null) {
            model.addAttribute("projects", projectService.getAllProjects());
            LOGGER.info("@GetMapping(\"/management/release\") - Accessing page");
            return "management/release";
        }

        // Project selected
        if(project != null) {
            session.setAttribute("project", project);
            Utils.sessionRecreate(session);
        }

        project = (long) session.getAttribute("project");
        model.addAttribute("projectId", project);

        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("commits", commitRepository.getReleasedCommitsByProject(project));
        return "management/release";
    }

    /**
     * Method opening specific commit based on identifier
     * @param model         Object for passing data to the UI
     * @param identifier    Identifier of commit for visualisation
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping(value = "/openCommit")
    public String openCommit(Model model,
                             @RequestParam(value = "identifier", required = false) String identifier,
                             RedirectAttributes redirectAttrs,
                             HttpSession session) {

        Commit commit = commitService.getCommitByIdentifier((long) session.getAttribute("project"), identifier);
        if(commit == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Commit doesn't exist");
            LOGGER.info("@PostMapping(\"/management/release/\") - Commit not found");
            return "redirect:/management/release";
        }

        model.addAttribute("selectedCommit", commit);
        model.addAttribute("committedBy", configurationPersonRelationRepository.getCommittedRelation(commit.getId()));
        return "management/commit";
    }

    /**
     * Method changing release attribute of specific commit
     * @param model         Object for passing data to the UI
     * @param identifier    Identifier of commit for attribute change
     * @param release       Value of attribute release
     * @param redirectAttrs Attributes for redirection
     * @param session       Session with attributes
     * @return              HTML template
     */
    @PostMapping(value = "/changeRelease")
    public String changeRelease(Model model,
                                @RequestParam(value = "identifier", required = false) String identifier,
                                @RequestParam(value = "release", required = false) boolean release,
                                RedirectAttributes redirectAttrs,
                                HttpSession session) {

        Commit commit = commitService.getCommitByIdentifier((long) session.getAttribute("project"), identifier);
        if(commit == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Commit doesn't exist");
            LOGGER.info("@PostMapping(\"/management/release/\") - Commit not found");
            return "redirect:/management/release";
        }

        if(release != commit.isRelease()) {
            commit.setRelease(release);
            commitService.saveCommit(commit);
        }

        LOGGER.info("@PostMapping(\"/management/person\") - Commit " + commit.getIdentifier() + " were unreleased");
        model.addAttribute("selectedCommit", commit);
        model.addAttribute("committedBy", configurationPersonRelationRepository.getCommittedRelation(commit.getId()));
        return "management/commit";
    }

    /**
     * Method for marking commit as not released
     * @param model         Object for passing data to the UI
     * @param submitId      ID of commit for unreleasing
     * @param redirectAttrs Attributes for redirection
     * @return
     */
    @PostMapping(value = "/unRelease")
    public String unRelease(Model model,
                            @RequestParam(value = "submitId", required = false) Long submitId,
                            RedirectAttributes redirectAttrs) {

        Commit commit = commitService.getCommitById(submitId);
        if(commit == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "ERROR: Submitted commit id doesn't exists");
            LOGGER.info("@PostMapping(\"/management/release/\") - Commit not found");
            return "redirect:/management/release";
        }

        commit.setRelease(false);
        commitService.saveCommit(commit);

        redirectAttrs.addFlashAttribute("successMessage", "Commit " + commit.getIdentifier() + " were unreleased");
        LOGGER.info("@PostMapping(\"/management/person\") - Commit " + commit.getIdentifier() + " were unreleased");
        return "redirect:/management/release";
    }

}
