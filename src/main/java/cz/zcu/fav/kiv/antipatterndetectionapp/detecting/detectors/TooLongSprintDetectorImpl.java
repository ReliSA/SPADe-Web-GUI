package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TooLongSprintDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(TooLongSprintDetectorImpl.class);

    private AntiPatternService antiPatternService = new AntiPatternServiceImpl();

    public final String configJsonFileName = "TooLongSprint.json";

    private AntiPattern antiPattern = antiPatternService.getAntiPatternFromJsonFile(configJsonFileName);

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_id.sql",
            "set_last_iteration_id.sql",
            "select_all_iterations_with_lengths_without_first_and_last.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    @Override
    public AntiPattern getAntiPatternModel() {
        this.antiPattern = antiPatternService.getAntiPatternFromJsonFile(configJsonFileName);
        return this.antiPattern;
    }

    @Override
    public List<String> getSqlFileNames() {
        return this.SQL_FILE_NAMES;
    }


    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    private Integer getMaxIterationLength() {
        this.antiPattern = antiPatternService.getAntiPatternFromJsonFile(configJsonFileName);
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxIterationLength").getValue()).intValue();
    }

    private Integer getMaxNumberOfTooLongIterations() {
        this.antiPattern = antiPatternService.getAntiPatternFromJsonFile(configJsonFileName);
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxNumberOfTooLongIterations").getValue()).intValue();
    }

    /**
     * Detection procedure:
     * 1) find all iterations of a given project
     * 2) remove the first and last iteration (these can be long due to the start of the project)
     * 3) find out their length (difference between start date and end date)
     * 4) if the iterations exceed the length of 21 days (adjustable threshold value), they are marked as too long
     * 5) if one or more iterations are found as long, then the anti pattern is detected
     *
     * @param project            analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // get configuration
        int maxIterationLength = getMaxIterationLength();
        int maxNumberOfTooLongIterations = getMaxNumberOfTooLongIterations();

        // auxiliary variables
        int numberOfLongIterations = 0;
        int totalCountOfIteration = 0;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
        List<Map<String, Object>> rs = resultSets.get(0);

        for (Map<String, Object> iterationLengths : rs) {
            totalCountOfIteration++;
            if (!iterationLengths.containsKey("iterationLength") || iterationLengths.get("iterationLength") == null)
                continue;
            int iterationLength = ((Number) iterationLengths.get("iterationLength")).intValue();

            if (iterationLength > maxIterationLength) {
                numberOfLongIterations++;
            }
        }
        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Count of iterations without first and last", String.valueOf(totalCountOfIteration)));
        resultDetails.add(new ResultDetail("Number of too long iterations", String.valueOf(numberOfLongIterations)));
        if (numberOfLongIterations > maxNumberOfTooLongIterations) {
            resultDetails.add(new ResultDetail("Conclusion", "One or more iteration is too long"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "All iterations in limit"));
        }

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, numberOfLongIterations > maxNumberOfTooLongIterations, resultDetails);
    }
}
