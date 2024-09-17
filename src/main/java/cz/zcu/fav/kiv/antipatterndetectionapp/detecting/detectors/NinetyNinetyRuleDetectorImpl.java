package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NinetyNinetyRuleDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    public final String configJsonFileName = "NinetyNinetyRule.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_all_iterations_with_sum_estimated_and_spent_time.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private double getMaxDivisionRange(Map<String, String> thresholds) {
        return new PositiveFloat(Float.parseFloat(thresholds.get("maxDivisionRange"))).floatValue();
    }

    private int getMaxBadDivisionLimit(Map<String, String> thresholds) {
        return new PositiveInteger(Integer.parseInt(thresholds.get("maxBadDivisionLimit"))).intValue();
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
     * Detection procedure
     * 1) for each iteration, make the sum of time spent and estimated over all activities
     * 2) Make a share of time spent / estimated time
     * 3) if all share results are less than 1.2 => all ok
     * 4) if the previous point does not work, then iterate over all shares
     * 5) if three iterations are found in a row, where estimates keep getting worse => detected
     *
     * @param project            analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {

        List<ResultDetail> resultDetails = new ArrayList<>();
        List<Double> divisionsResults = new ArrayList<>();
        boolean isAllInRange = true;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
        for (List<Map<String, Object>> rs : resultSets) {
            for (Map<String, Object> map : rs) {
                Double resultDivision = (Double) map.get("timeDivision");
                // some divisions can by null
                if (resultDivision == null) {
                    continue;
                }
                divisionsResults.add(resultDivision);
                // if is one division is out of range set boolean to false
                if (resultDivision > getMaxDivisionRange(thresholds)) {
                    isAllInRange = false;
                }
            }
        }

        if (isAllInRange) {
            resultDetails.add(new ResultDetail("Conclusion",
                    "All divisions of estimated and spent time are in range"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }

        int counterOverEstimated = 0;
        for (Double divisionResult : divisionsResults) {
            if (divisionResult > getMaxDivisionRange(thresholds)) {
                counterOverEstimated++;
            } else {
                counterOverEstimated = 0;
            }

            if (counterOverEstimated > getMaxBadDivisionLimit(thresholds)) {
                resultDetails.add(new ResultDetail("Conclusion",
                        getMaxBadDivisionLimit(thresholds) + " or more consecutive iterations has a bad trend in estimates"));
                return new QueryResultItem(this.antiPattern, true, resultDetails);
            }

        }

        resultDetails.add(new ResultDetail("Conclusion",
                "No significant trend was found for estimation and time worked on activities"));
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
