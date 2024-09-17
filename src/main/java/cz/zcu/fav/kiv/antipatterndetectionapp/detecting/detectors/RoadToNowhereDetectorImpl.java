package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RoadToNowhereDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    public final String configJsonFileName = "RoadToNowhere.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_start_date.sql",
            "set_second_iteration_start_date.sql",
            "set_number_of_activities_with_substrings.sql",
            "set_number_of_wiki_pages_with_substrings.sql",
            "select_number_of_activities_and_wiki_pages_with_substrings.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithProjectPlan(Map<String, String> thresholds) {
        return new PositiveInteger(Integer.parseInt(thresholds.get("minNumberOfWikiPagesWithProjectPlan"))).intValue();
    }

    private int getMinNumberOfActivitiesWithProjectPlan(Map<String, String> thresholds) {
        return new PositiveInteger(Integer.parseInt(thresholds.get("minNumberOfActivitiesWithProjectPlan"))).intValue();
    }

    private List<String> getSearchSubstringsWithProjectPlan(Map<String, String> thresholds) {
        return Arrays.asList(thresholds.get("searchSubstringsWithProjectPlan").split("\\|\\|"));
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
     * 1) for each project, try to find out if it contains any wiki pages with the project plan
     * 2) further try to find activities that would indicate that a project plan has been created
     * 3) if no activity or wiki page is found, the anti-pattern is detected
     *
     * @param project            analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {

        /* Init values */
        List<ResultDetail> resultDetails = new ArrayList<>();
        int numberOfIssuesForProjectPlan = 0;
        int numberOfWikiPagesForProjectPlan = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project,
                    Utils.fillQueryWithSearchSubstrings(this.sqlQueries, getSearchSubstringsWithProjectPlan(thresholds)));
            if (rs != null) {
                while (rs.next()) {
                    numberOfIssuesForProjectPlan = rs.getInt("numberOfActivitiesWithSubstrings");
                    numberOfWikiPagesForProjectPlan = rs.getInt("numberOfWikiPagesWithSubstrings");
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Number of issues for creating project plan", String.valueOf(numberOfIssuesForProjectPlan)));
        resultDetails.add(new ResultDetail("Number of wiki pages for creating project plan", String.valueOf(numberOfWikiPagesForProjectPlan)));

        if (numberOfIssuesForProjectPlan >= getMinNumberOfActivitiesWithProjectPlan(thresholds) || numberOfWikiPagesForProjectPlan >= getMinNumberOfWikiPagesWithProjectPlan(thresholds)) {
            resultDetails.add(new ResultDetail("Conclusion", "Found some activities or wiki pages for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "No wiki pages and activities for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }
    }
}
