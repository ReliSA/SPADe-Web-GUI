package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.detection;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ConfigurationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserDetectionDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration.ConfigService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ClassToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DetectionServiceImpl implements DetectionService{
    @Autowired
    ConfigService configService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private AntiPatternManager antiPatternManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public Query getAllProjectsAndAntipatterns() {
        ClassToDto<Project, ProjectDto> classToDto = new ProjectToDto();
        Query q = new Query(classToDto.convert(projectService.getAllProjects()), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));

        return q;
    }

    @Override
    public List<QueryResult> analyze(UserDetectionDto detectionRequest) {
        //User user = userService.getUserByName(detectionRequest.getUserName());
        int configurationId = detectionRequest.getConfigurationId();
        //String currentConfigurationName = this.configService.getConfigurationName(user.getId(), configurationId);
        Configuration cfg = this.configService.getConfigurationById(configurationId);

        //namapovat cfg do jsonu
        Map<String, Map<String,String>> currentConfiguration = JSONBuilder.createMapFromString(cfg.getConfig());

        String[] selectedProjects = detectionRequest.getSelectedProjects();
        String[] selectedAntiPatterns = detectionRequest.getSelectedAntipatterns();
        List<QueryResult> results = antiPatternManager.analyze(selectedProjects, selectedAntiPatterns, currentConfiguration);

       /* List<QueryResultItem> i = new ArrayList<>();
        Map<String,Threshold> thresholds = new HashMap<>();
        thresholds.put("divisionOfIterationsWithRetrospective",new Threshold("divisionOfIterationsWithRetrospective","Division of iterations with retrospective","Minimum percentage of the total number of iterations with a retrospective (0,100)","Percentage must be float number between 0 and 100","Percentage"));
        i.add(new QueryResultItem(new AntiPattern(3,"Business As Usual","BusinessAsUsual","Absence of a retrospective after individual iterations or after the completion project.")))
        QueryResult q = new QueryResult(new Project("Rozšíření nástroje IMiGEr (KIV) - HKMM",""),new ArrayList<>());
        */


        return results;
    }
}
