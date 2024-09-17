package cz.zcu.fav.kiv.antipatterndetectionapp.controller;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ConfigurationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.UserAccountService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ClassToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * This class contains all endpoints of the web application.
 */
@Controller
public class AppController {

    private final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private AntiPatternManager antiPatternManager;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ConfigurationService configurationService;

    private final ClassToDto<Project, ProjectDto> classToDto = new ProjectToDto();

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        return "main-page";
    }

    /**
     * This method is called by the GET method and initializes
     * the main page of the application (index). Loads all projects
     * stored in the db and all implemented AP.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/detect")
    public String index(Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        LOGGER.info("@GetMapping(\"/\") - Accessing main page");
        return "index";
    }

    /**
     * Method for obtaining project by ID.
     *
     * @param id    project ID
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/projects/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("project", projectService.getProjectById(id));
        LOGGER.info("@GetMapping(\"projects/" + id + ")");
        return "project";
    }

    /**
     * Method for obtaining all AP.
     * @return list of AP
     */
    @GetMapping("/anti-patterns")
    public @ResponseBody List<AntiPattern> getAllAntiPatterns(Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        LOGGER.info("GetMapping(\"/anti-patterns\") and obtaining all APs.");
        return antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
    }

    /**
     * Method for obtaining AP by ID.
     *
     * @param id    AP ID
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/anti-patterns/{id}")
    public String getAntiPatternById(@PathVariable Long id, Model model, HttpSession session) {
        String currentConfigurationName = configurationGetFromSession(session);
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("antiPattern", antiPatternService.getAntiPatternById(id).getAntiPatternModel());
        model.addAttribute("description", antiPatternService.getDescriptionFromCatalogue(id));
        model.addAttribute("operationalizationText", antiPatternService.getOperationalizationText(antiPatternService.getAntiPatternById(id).getAntiPatternModel().getName()));
        model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));
        LOGGER.info("GetMapping(\"/anti-patterns/ " + id + ") and obtaining AP by ID");
        return "anti-pattern";
    }

    /**
     * Method that processes requirements for analyzing selected projects and AP.
     *
     * @param model                object for passing data to the UI
     * @param selectedProjects     selected project to analyze
     * @param selectedAntiPatterns selected AP to analyze
     * @return html file name for thymeleaf template
     */
    @PostMapping("/analyze")
    public String analyze(Model model,
                          @RequestParam(value = "selectedProjects", required = false) String[] selectedProjects,
                          @RequestParam(value = "selectedAntiPatterns", required = false) String[] selectedAntiPatterns,
                          HttpSession session
    ) {

        String currentConfigurationName = configurationGetFromSession(session);

        if (currentConfigurationName == null) {
            model.addAttribute("errorMessage", "No configuration was found!");
            model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
            return "index";
        }

        if (selectedProjects == null) {
            model.addAttribute("errorMessage", "No project selected." +
                    " Select at least one project.");
            model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
            LOGGER.warn("@PostMapping(\"/analyze\") - Processing requirements for analyzing selected projects and AP with an error. " +
                    "selectedProjects are null!");
            return "index";
        }

        if (selectedAntiPatterns == null) {
            model.addAttribute("errorMessage", "No anti-pattern selected." +
                    " Select at least one anti-pattern.");
            model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
            LOGGER.warn("@PostMapping(\"/analyze\") - Processing requirements for analyzing selected projects and AP with an error. " +
                    "selectedAntiPatterns are null!");
            return "index";
        }



        Map<String, Map<String, String>> currentConfiguration = configurationService.getConfigurationByName(currentConfigurationName);
        List<QueryResult> results = antiPatternManager.analyze(selectedProjects, selectedAntiPatterns, currentConfiguration);
        antiPatternService.saveAnalyzedProjects(selectedProjects, selectedAntiPatterns);
        antiPatternService.saveResults(results);
        antiPatternService.setConfigurationChanged(false);

        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("queryResults", results);
        model.addAttribute("recalculationTime", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));


        LOGGER.info("@PostMapping(\"/analyze\") - Processing SUCCESSFULLY requirements for analyzing selected projects and AP.");
        return "result";
    }

    /**
     * Method for checking the change of configuration values ​​after pressing the back button in the browser.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/analyze")
    public String analyzeGet(Model model) {
        if (antiPatternService.isConfigurationChanged()) {
            model.addAttribute("isConfigurationChanged", true);
        }
        antiPatternService.setConfigurationChanged(false);

        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("queryResults", antiPatternService.getResults());
        model.addAttribute("recalculationTime", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));

        LOGGER.info("@GetMapping(\"/analyze\") - ??");
        return "result";
    }

    /**
     * Method for showing about page.
     *
     * @return html file name for thymeleaf template
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        LOGGER.info("@GetMapping(\"/about\") - Accessing about page");
        return "about";
    }

    /**
     * Method for getting all configuration from app and set it to the model class.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/configuration")
    public String configuration(Model model, HttpSession session) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("antiPatterns", antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));

        String currentConfigurationName = configurationGetFromSession(session);
        model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));
        return "configuration";
    }

    /**
     * Method of saving changes of current configuration
     *
     * @param model           object for passing data to the UI
     * @param thresholdValues changed configuration values
     * @param thresholdNames  changed configuration names
     * @return html file name for thymeleaf template
     */
    @PostMapping(value = "/configuration", params = "configuration-save-button")
    public String configurationSavePost(Model model,
                                        @RequestParam(value = "thresholdValues", required = false) String[] thresholdValues,
                                        @RequestParam(value = "thresholdNames", required = false) String[] thresholdNames,
                                        @RequestParam(value = "antiPatternNames", required = false) String[] antiPatternNames,
                                        HttpSession session, RedirectAttributes redirectAttributes) {

        String currentConfigurationName = configurationGetFromSession(session);

        Query query = new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));

        List<String> wrongParameters = configurationService.saveNewConfiguration(query.getAntiPatterns(), currentConfigurationName, antiPatternNames, thresholdNames, thresholdValues, true);

        if (wrongParameters.isEmpty()) {
            model.addAttribute("successMessage", "All configuration values have been successfully saved.");
            LOGGER.info("@PostMapping(value = \"/configuration\", params = \"configuration-save-button\") - " +
                    "input parameters were successfully processed...");
        } else {
            model.addAttribute("errorMessage", "One or more configuration values are not in correct format, see messages below.");
            antiPatternService.setErrorMessages(query.getAntiPatterns(), wrongParameters);
            LOGGER.warn("@PostMapping(value = \"/configuration\", params = \"configuration-save-button\") ended with an error!" +
                    "Some parameters seem to be wrong!");
        }

        model.addAttribute("query", query);

        model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));
        LOGGER.info("@PostMapping(value = \"/configuration\", params = \"configuration-save-button\") - Save Button was pressed");
        return "configuration";
    }

    /**
     * Method for saving full new configuration
     *
     * @param model           object for passing data to the UI
     * @param thresholdValues changed configuration values
     * @param thresholdNames  changed configuration names
     * @return html file name for thymeleaf template
     */
    @PostMapping(value = "/configuration", params = "configuration-save-as-button")
    public String configurationSaveAsPost(Model model,
                                    @RequestParam(value = "thresholdValues", required = false) String[] thresholdValues,
                                    @RequestParam(value = "thresholdNames", required = false) String[] thresholdNames,
                                    @RequestParam(value = "antiPatternNames", required = false) String[] antiPatternNames,
                                    @RequestParam(value = "configuration-save-as-input") String newConfigName,
                                    HttpSession session, RedirectAttributes redirectAttributes) {

        Query query = new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));
        String currentConfigurationName = configurationGetFromSession(session);

        List<String> allConfigurationNames = configurationService.getAllConfigurationNames();
        if (newConfigName == null || newConfigName.isEmpty() || newConfigName.isBlank() ||  allConfigurationNames.contains(newConfigName)) {
            model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));
            model.addAttribute("errorMessage", "Configuration name is not possible.");
            model.addAttribute("query", query);
            LOGGER.error("@PostMapping(value = \"/configuration\", params = \"configuration-save-as-button\") - " +
                    "new configuration could not be saved because the name was not entered!");
            return "configuration";
        }

        List<String> wrongParameters = configurationService.saveNewConfiguration(query.getAntiPatterns(), newConfigName, antiPatternNames, thresholdNames, thresholdValues, true);

        if (wrongParameters.isEmpty()) {
            model.addAttribute("successMessage", "New configuration has been successfully saved.");
            session.setAttribute("configuration", newConfigName);
            model.addAttribute("configurations", configurationService.getConfigurationByName(newConfigName));
            LOGGER.warn("@PostMapping(value = \"/configuration\", params = \"configuration-save-as-button\") - " +
                    "some parameters seem to be wrong!");
        } else {
            model.addAttribute("errorMessage", "One or more configuration values are not in correct format, see messages below.");
            antiPatternService.setErrorMessages(query.getAntiPatterns(), wrongParameters);
            model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));
            LOGGER.info("@PostMapping(value = \"/configuration\", params = \"configuration-save-as-button\") - " +
                    "parameters were successfully processed...");
        }

        model.addAttribute("query", query);

        model.addAttribute("configurationList", configurationsGetList(session));
        model.addAttribute("selectedConfiguration", session.getAttribute("configuration"));

        LOGGER.info("@PostMapping(value = \"/configuration\", params = \"configuration-save-as-button\") - " +
                "everything successfully processed... Returning configuration.html");
        return "configuration";
    }

    /**
     * Method for saving changes of one AP in current configuration
     *
     * @param model           object for passing data to the UI
     * @param id              id of AP
     * @param thresholdValues new config values
     * @param thresholdNames  configuration names
     * @param redirectAttrs   attributes for redirection
     * @return redirected html file name for thymeleaf template
     */
    @PostMapping("/anti-patterns/{id}")
    public String antiPatternsPost(Model model,
                                   @PathVariable Long id,
                                   @RequestParam(value = "thresholdValues", required = false) String[] thresholdValues,
                                   @RequestParam(value = "thresholdNames", required = false) String[] thresholdNames,
                                   @RequestParam(value = "antiPatternNames", required = false) String[] antiPatternNames,
                                   HttpSession session,
                                   RedirectAttributes redirectAttrs) {

        String currentConfigurationName = configurationGetFromSession(session);
        List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());

        AntiPattern antiPattern = antiPatternService.antiPatternToModel(antiPatternService.getAntiPatternById(id));
        List<String> wrongParameters = configurationService.saveNewConfiguration(antiPatterns, currentConfigurationName, antiPatternNames, thresholdNames, thresholdValues, false);


        if (wrongParameters.isEmpty()) {
            redirectAttrs.addFlashAttribute("successMessage", "All threshold values has been successfully saved.");
            LOGGER.info("@PostMapping(\"/anti-patterns/{id}\") - Success");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "One or more configuration values are not in correct format, see messages below.");
            antiPatternService.setErrorMessages(antiPattern, wrongParameters);
            LOGGER.warn("@PostMapping(\"/anti-patterns/{id}\") - Some parameters seem to be wrong!");
        }

        model.addAttribute("antiPatterns", antiPattern);
        model.addAttribute("configurations", configurationService.getConfigurationByName(currentConfigurationName));

        LOGGER.info("@PostMapping(\"/anti-patterns/{id}\") - redirecting to current anti pattern id");
        return "redirect:/anti-patterns/{id}";
    }

    /**
     * Method for storing operationalization detail for individual AP
     *
     * @param model         object for passing data to the UI
     * @param id            id of AP
     * @param innerText     operationalization text (HTML)
     * @param redirectAttrs attributes for redirection
     * @return redirected html file name for thymeleaf template
     */
    @PostMapping("/anti-patterns/{id}/operationalization")
    public String antiPatternsPost(Model model,
                                   @PathVariable Long id,
                                   @RequestParam(value = "contentTextArea", required = false) String innerText,
                                   RedirectAttributes redirectAttrs) {

        AntiPattern antiPattern = antiPatternService.antiPatternToModel(antiPatternService.getAntiPatternById(id));

        String thePath = antiPatternService.getOperationalizationFilePath(antiPattern.getName());
        try {
            Jsoup.clean(innerText, Safelist.basic()); // xss attack prevention
            BufferedWriter writer = new BufferedWriter(new FileWriter(thePath));
            writer.write(innerText);
            writer.close();
            redirectAttrs.addFlashAttribute("successMessage", "Operationalization detail has been successfully saved.");
        } catch (Exception e) {
            LOGGER.error("@PostMapping(\"/anti-patterns/{id}/operationalization\") - " +
                    "An error has occurred while trying to create new BufferedWriter or FileWriter!");
        }
        return "redirect:/anti-patterns/{id}";
    }

    /**
     * Method for getting image from operationalization images folder
     *
     * @param imageName Name of the image
     * @return image as a byte array
     * @throws Exception If image is not in the folder
     */
    @GetMapping("/operationalizations/images/{imageName}")
    public @ResponseBody byte[] imageGet(@PathVariable String imageName) throws Exception {
        File f = new File(antiPatternService.getOperationalizationImageFilePath(imageName));
        return Files.readAllBytes(f.toPath());
    }

    /**
     * Method for uploading an image to the operationalization images folder
     *
     * @param file image to upload
     * @return result
     */
    @PostMapping("/uploadImage")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();

        if (new File(antiPatternService.getOperationalizationImageFilePath(fileName)).isFile()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            file.transferTo(new File(antiPatternService.getOperationalizationImageFilePath(fileName)));
        } catch (Exception e) {
            LOGGER.error("An error with uploading images has occurred!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("File uploaded successfully.");
    }

    /**
     * Method for showing login page.
     *
     * @return html file name for thymeleaf template
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        return "login";
    }

    /**
     * Processing login details
     *
     * @param session       current session
     * @param nameInput     input field for name
     * @param passInput     input field for password
     * @param redirectAttrs redirect attributes for adding flash message
     * @return html file name for thymeleaf template
     */
    @PostMapping("/login")
    public String loginProcess(HttpSession session,
                               @RequestParam(value = "nameInput", required = false) String nameInput,
                               @RequestParam(value = "passInput", required = false) String passInput,
                               RedirectAttributes redirectAttrs) {

        if (userAccountService.checkCredentials(nameInput, passInput)) {
            session.setAttribute("user", nameInput);
            return "redirect:/";
        }

        redirectAttrs.addFlashAttribute("errorMessage", "Invalid details");
        return "redirect:/login";
    }

    /**
     * Getting the name of the logged user from the session
     *
     * @param model   object for passing data to the UI
     * @param session current session
     * @return html file name for thymeleaf template
     */
    @GetMapping("/userLogged")
    public String userLogged(Model model, HttpSession session) {
        String user = (String) session.getAttribute("user");
        model.addAttribute("user", user);
        return "redirect:/";
    }

    /**
     * Processing logout, removing user from session
     *
     * @param model   object for passing data to the UI
     * @param session current session
     * @return html file name for thymeleaf template
     */
    @GetMapping("/logout")
    public String userLogout(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
        session.removeAttribute("user");
        session.removeAttribute("configuration");
        return "redirect:/";
    }

    /**
     * Model attribute for getting list of configurations
     *
     * @param session current session
     * @return list of configurations
     */
    @ModelAttribute("configurationList")
    public List<String> configurationsGetList(HttpSession session) {
        List<String> configurationList;

        if (session.getAttribute("user") != null)
            configurationList = configurationService.getAllConfigurationNames();
        else
            configurationList = configurationService.getDefaultConfigurationNames();

        return configurationList;
    }

    /**
     * Model attribute for getting current configuration
     *
     * @param session current session
     * @return name of current configuration
     */
    @ModelAttribute("selectedConfiguration")
    public String configurationGetFromSession(HttpSession session) {
        if (session.getAttribute("configuration") != null)
            return session.getAttribute("configuration").toString(); // return configuration stored in session
        else {
            List<String> configurationList = configurationService.getDefaultConfigurationNames();
            if (configurationList.size() == 0)
                return null;

            return configurationList.get(0); // return first item from the list of default configurations
        }
    }

    /**
     * Processing select of configuration from select box
     *
     * @param session            current session
     * @param selectedConfigName name of selected configuration
     * @param redirectAttrs      attributes for redirection
     * @return html page for redirect
     */
    @PostMapping("/setSelectedConfiguration")
    public String setSelectedConfiguration(HttpSession session,
                                           @RequestParam(value = "current-configuration-select", required = false) String selectedConfigName,
                                           RedirectAttributes redirectAttrs) {

        session.setAttribute("configuration", selectedConfigName); // storing selected configuration to session

        return "redirect:/configuration";
    }

    /**
     * Checking if configuration is default and editable by user
     *
     * @param model             object for passing data to the UI
     * @param session           current session
     * @param configurationName name of the curren configuration
     * @return html template
     */
    @GetMapping("/isConfigEditable/{configurationName}")
    public String isConfigEditableForUser(Model model, HttpSession session, @PathVariable String configurationName) {
        List<String> defaultConfigurationNames = configurationService.getDefaultConfigurationNames();

        for (int i = 0; i < defaultConfigurationNames.size(); i++) {
            if (defaultConfigurationNames.get(i).equals(configurationName))
                model.addAttribute("default", "true"); // configuration is default
        }

        String user = (String) session.getAttribute("user");
        model.addAttribute("user", user); // user is logged

        return "redirect:/configuration";
    }


}
