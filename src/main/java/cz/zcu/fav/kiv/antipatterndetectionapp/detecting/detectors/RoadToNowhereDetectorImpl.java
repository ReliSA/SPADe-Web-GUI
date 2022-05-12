package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoadToNowhereDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(5L,
            "Road To Nowhere",
            "RoadToNowhere",
            "The project is not sufficiently planned and therefore " +
                    "takes place on an ad hoc basis with an uncertain " +
                    "outcome and deadline. There is no project plan in the project.",
            new HashMap<>() {{
                put("minNumberOfWikiPagesWithProjectPlan", new Configuration<Integer>("minNumberOfWikiPagesWithProjectPlan",
                        "Minimum number of wiki pages with project plan",
                        "Number of wiki pages", 1));
                put("minNumberOfActivitiesWithProjectPlan", new Configuration<Integer>("minNumberOfActivitiesWithProjectPlan",
                        "Minimum number of activities with project plan",
                        "Number of activities", 1));
            }},
            "Road_To_Nowhere.md");

    private final String sqlFileName = "road_to_nowhere.sql";
    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithProjectPlan() {
        return (int) antiPattern.getConfigurations().get("minNumberOfWikiPagesWithProjectPlan").getValue();
    }

    private int getMinNumberOfActivitiesWithProjectPlan() {
        return (int) antiPattern.getConfigurations().get("minNumberOfActivitiesWithProjectPlan").getValue();
    }

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.sqlFileName;
    }

    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    /**
     * Postup detekce:
     *      1) u každého projektu zkusit nalézt jestli obsahuje nějaké wiki stránky s projektovým plánem
     *      2) dále zkusit najít aktivity, které by naznačovali, že vznikl nějaký projektový plán
     *      3) pokud nebude nalezena žádná aktivita nebo wiki stránka, tak je antivzor detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        /* Init values */
        List<ResultDetail> resultDetails = new ArrayList<>();
        int numberOfIssuesForProjectPlan = 0;
        int numberOfWikiPagesForProjectPlan = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, this.sqlQueries);
            if (rs != null) {
                while (rs.next()) {
                    numberOfIssuesForProjectPlan = rs.getInt("numberOfIssuesForProjectPlan");
                    numberOfWikiPagesForProjectPlan = rs.getInt("numberOfWikiPagesForProjectPlan");
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Number of issues for creating project plan", String.valueOf(numberOfIssuesForProjectPlan)));
        resultDetails.add(new ResultDetail("Number of wiki pages for creating project plan", String.valueOf(numberOfWikiPagesForProjectPlan)));

        if( numberOfIssuesForProjectPlan >= getMinNumberOfActivitiesWithProjectPlan() || numberOfWikiPagesForProjectPlan >= getMinNumberOfWikiPagesWithProjectPlan()) {
            resultDetails.add(new ResultDetail("Conclusion", "Found some activities or wiki pages for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "No wiki pages and activities for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }
    }
}
