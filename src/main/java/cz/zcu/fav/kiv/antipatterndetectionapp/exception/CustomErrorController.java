package cz.zcu.fav.kiv.antipatterndetectionapp.exception;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ClassToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private ProjectService projectService;


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        String errorPage = "error";

        final Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);


        if (statusCode != null) {
            final int code = Integer.parseInt(statusCode.toString());

            if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorPage = "error/500";
            } else if (code == HttpStatus.NOT_FOUND.value()) {
                errorPage = "error/404";
            } else if (code == HttpStatus.UNAUTHORIZED.value()) {
                errorPage = "error/403";
            }
        }

        ClassToDto<Project, ProjectDto> classToDto = new ProjectToDto();
        model.addAttribute("query", new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        model.addAttribute("antiPatterns", antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));

        if (exception != null) {
            model.addAttribute("errorMsg", exception.getLocalizedMessage());
        }

        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
