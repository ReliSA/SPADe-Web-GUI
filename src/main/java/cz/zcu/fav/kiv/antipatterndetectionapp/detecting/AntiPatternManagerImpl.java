package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AntiPatternManagerImpl implements AntiPatternManager {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Override
    public List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns, Map<String, Map<String, String>> configuration) {
        //not null is assured via controllers and dtos
        //but the array might be empty
        //the analysis should not run
        if(selectedProjects.length == 0 || selectedAntiPatterns.length == 0){
            return new ArrayList<>();
        }

        return this.analyze(projectService.getAllProjectsForGivenIds(Utils.arrayOfStringsToArrayOfLongs(selectedProjects)),
                antiPatternService.getAllAntiPatternsForGivenIds(Utils.arrayOfStringsToArrayOfLongs(selectedAntiPatterns)), configuration);
    }

    /**
     * This method iterate over each project and detecting all AP.
     * @param projects analyzed projects
     * @param antiPatternDetectors Ap detectoros
     * @return List of results
     */
    private List<QueryResult> analyze(List<Project> projects, List<AntiPatternDetector> antiPatternDetectors, Map<String, Map<String, String>> configuration) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        List<QueryResult> queryResults = new ArrayList<>();


        for (Project project : projects) {
            QueryResult queryResult = new QueryResult();
            ProjectToDto projectToDto = new ProjectToDto();
            queryResult.setProject(projectToDto.convert(project));
            List<QueryResultItem> queryResultItems = new ArrayList<>();

            for (AntiPatternDetector antiPattern : antiPatternDetectors) {
                String antiPatternName = antiPattern.getAntiPatternModel().getName();

                Map<String, String> thresholds = null;

                if(configuration != null)
                    thresholds = configuration.get(antiPatternName);

                queryResultItems.add(antiPattern.analyze(project, databaseConnection, thresholds));
            }
            queryResult.setQueryResultItems(queryResultItems);
            queryResults.add(queryResult);

        }

        databaseConnection.closeConnection();

        return queryResults;
    }
}
