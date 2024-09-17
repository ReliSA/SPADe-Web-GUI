package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VaryingSprintLengthDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(VaryingSprintLengthDetectorImpl.class);

    public final String configJsonFileName = "VaryingSprintLength.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_id.sql",
            "set_last_iteration_id.sql",
            "select_all_iterations_with_lengths_without_first_and_last.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private Integer getMaxDaysDifference(Map<String, String> thresholds) {
        return new PositiveInteger(Integer.parseInt(thresholds.get("maxDaysDifference"))).intValue();
    }

    private Integer getMaxIterationChanged(Map<String, String> thresholds) {
        return new PositiveInteger(Integer.parseInt(thresholds.get("maxIterationChanged"))).intValue();
    }

    @Override
    public String getJsonFileName(){
        return this.configJsonFileName;
    }

    @Override
    public void setAntiPattern(AntiPattern antiPattern) {
        this.antiPattern = antiPattern;
    }

    @Override
    public AntiPattern getAntiPatternModel() {
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

    /**
     * Detection procedure:
     * 1) find all iterations for a given project sorted by name (start date has strange values ​​and iterations do not follow each other)
     * 2) removal of the first and last iteration due to possible fluctuations
     * 3) find out their length (difference between start date and end date)
     * 4) Always compare two consecutive iterations
     * 5) if the length of the compared iterations differs by more than 7 days, the counter is raised
     * 6) if the counter exceeds the value 2, then the anti patern is detected
     *
     * @param project            analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {

        // init values
        List<ResultDetail> resultDetails = new ArrayList<>();
        int iterationLengthChanged = 0;
        int numberOfIterations = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, this.sqlQueries);
            if (rs != null) {
                int firstIterationLength = Integer.MIN_VALUE;
                int secondIterationLength;
                while (rs.next()) {
                    int iterationLength = rs.getInt("iterationLength");
                    numberOfIterations++;
                    if (firstIterationLength == Integer.MIN_VALUE) {
                        firstIterationLength = iterationLength;
                        continue;
                    } else {
                        secondIterationLength = iterationLength;
                    }

                    if (Math.abs(firstIterationLength - secondIterationLength) >= getMaxDaysDifference(thresholds)) {
                        iterationLengthChanged = iterationLengthChanged + 1;
                    }
                    firstIterationLength = secondIterationLength;
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Maximum iteration length change", String.valueOf(getMaxIterationChanged(thresholds))));
        resultDetails.add(new ResultDetail("Count of iterations", String.valueOf(numberOfIterations)));
        resultDetails.add(new ResultDetail("Iteration length changed", String.valueOf(iterationLengthChanged)));


        if (iterationLengthChanged > getMaxIterationChanged(thresholds)) {
            resultDetails.add(new ResultDetail("Conclusion", "Iteration length changed significantly too often"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "Varying iteration length is all right"));
        }

        return new QueryResultItem(this.antiPattern, (iterationLengthChanged > getMaxIterationChanged(thresholds)), resultDetails);
    }
}
