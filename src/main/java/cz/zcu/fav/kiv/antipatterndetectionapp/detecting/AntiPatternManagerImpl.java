package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AntiPatternManagerImpl implements AntiPatternManager {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Override
    public List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns) {

        return this.analyze(projectService.getAllProjectsForGivenIds(Utils.arrayOfStringsToArrayOfLongs(selectedProjects)),
                antiPatternService.getAllAntiPatternsForGivenIds(Utils.arrayOfStringsToArrayOfLongs(selectedAntiPatterns)));
    }

    /**
     * This method iterate over each project and detecting all AP.
     * @param projects analyzed projects
     * @param antiPatternDetectors Ap detectoros
     * @return List of results
     */
    private List<QueryResult> analyze(List<Project> projects, List<AntiPatternDetector> antiPatternDetectors) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        List<QueryResult> queryResults = new ArrayList<>();


        for (Project project : projects) {
            QueryResult queryResult = new QueryResult();
            queryResult.setProject(project);
            List<QueryResultItem> queryResultItems = new ArrayList<>();
            for (AntiPatternDetector antiPattern : antiPatternDetectors) {
                queryResultItems.add(antiPattern.analyze(project, databaseConnection));
            }
            queryResult.setQueryResultItems(queryResultItems);
            queryResults.add(queryResult);

        }

        databaseConnection.closeConnection();

        return queryResults;
    }
}
