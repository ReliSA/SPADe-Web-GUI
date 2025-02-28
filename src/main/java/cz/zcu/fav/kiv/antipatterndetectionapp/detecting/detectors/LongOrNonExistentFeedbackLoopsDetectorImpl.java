package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

public class LongOrNonExistentFeedbackLoopsDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(LongOrNonExistentFeedbackLoopsDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(6L,
            "Long Or Non Existent Feedback Loops",
            "LongOrNonExistentFeedbackLoops",
            "Long spacings between customer feedback or no feedback. The customer " +
                    "enters the project and sees the final result. In the end, the customer " +
                    "may not get what he really wanted. With long intervals of feedback, " +
                    "some misunderstood functionality can be created and we have to spend " +
                    "a lot of effort and time to redo it. ",
            new HashMap<>() {{
                put("divisionOfIterationsWithFeedbackLoop", new Configuration<Percentage>("divisionOfIterationsWithFeedbackLoop",
                        "Division of iterations with feedback loop",
                        "Minimum percentage of the total number of iterations with feedback loop (0,100)",
                        "Percentage must be float number between 0 and 100",
                        new Percentage(50.00f)));
                put("maxGapBetweenFeedbackLoopRate", new Configuration<PositiveFloat>("maxGapBetweenFeedbackLoopRate",
                        "Maximum gap between feedback loop rate",
                        "Value that multiplies average iteration length for given project. Result" +
                                " is maximum threshold value for gap between feedback loops in days.",
                        "Maximum gap between feedback loop rate must be positive float number",
                        new PositiveFloat(2f)));
                put("searchSubstringsWithFeedbackLoop", new Configuration<String>("searchSubstringsWithFeedbackLoop",
                        "Search substrings with feedback loop",
                        "Substring that will be search in wikipages and activities",
                        "Maximum number of substrings is ten and must not starts and ends with characters ||",
                        "%schůz%zákazník%" + Constants.SUBSTRING_DELIMITER +
                                "%předvedení%zákazník%" + Constants.SUBSTRING_DELIMITER +
                                "%zákazn%demo%" + Constants.SUBSTRING_DELIMITER +
                                "%schůz%zadavat%" + Constants.SUBSTRING_DELIMITER +
                                "%inform%schůz%" + Constants.SUBSTRING_DELIMITER +
                                "%zákazn%" + Constants.SUBSTRING_DELIMITER +
                                "%zadavatel%"));
            }}
    );

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_number_of_iterations.sql",
            "select_average_iterations_length.sql",
            "select_number_of_iterations_with_substrings_in_activity.sql",
            "select_all_activities_with_substrings_and_last_modified_date_as_end_date.sql",
            "select_project_start_date.sql",
            "select_project_end_date.sql",
            "select_all_iterations_that_contains_wikipages_with_substrings.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private float getDivisionOfIterationsWithFeedbackLoop() {
        return ((Percentage) antiPattern.getConfigurations().get("divisionOfIterationsWithFeedbackLoop").getValue()).getValue();
    }

    private float getMaxGapBetweenFeedbackLoopRate() {
        return ((PositiveFloat) antiPattern.getConfigurations().get("maxGapBetweenFeedbackLoopRate").getValue()).floatValue();
    }

    private List<String> getSearchSubstringsWithFeedbackLoop() {
        return Arrays.asList(((String) antiPattern.getConfigurations().get("searchSubstringsWithFeedbackLoop").getValue()).split("\\|\\|"));
    }

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    public List<String> getSqlFileNames() {
        return this.SQL_FILE_NAMES;
    }

    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    /**
     * Detection procedure
     * 1) find all activities that could represent a customer demo (the name will include substring)
     * 2) find out the average length of iterations
     * 3) find out the number of iterations
     * 4) first compare with the number of iterations => iterations and the activities found
     * should be ideally equal (they can be smaller or larger but not much smaller)
     * 5) for every two consecutive activities, make a difference of dates and compare with
     * the average length of the iteration => the difference should not differ much from t
     * he average length of the iteration
     * 6) if a small number of found activities is detected in point 4)
     * (the team does not do activities for meetings and can only record to the wiki)
     * 7) Find all wiki pages and make a join when they have changed
     * (one page can be used for multiple meetings) with the appropriate name
     * 8) do a group by day
     *
     * @param project analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // init values
        long totalNumberIterations = 0;
        int averageIterationLength = 0;
        int numberOfIterationsWhichContainsAtLeastOneActivityForFeedback = 0;
        int numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback = 0;
        List<Date> feedbackActivityEndDates = new ArrayList<>();
        List<Date> feedbackWikiPagesEndDates = new ArrayList<>();
        Date projectStartDate = null;
        Date projectEndDate = null;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project,
                Utils.fillQueryWithSearchSubstrings(this.sqlQueries, getSearchSubstringsWithFeedbackLoop()));
        for (int i = 0; i < resultSets.size(); i++) {
            List<Map<String, Object>> rs = resultSets.get(i);

            switch (i) {
                case 0:
                    totalNumberIterations = (long) rs.get(0).get("numberOfIterations");
                    break;
                case 1:
                    averageIterationLength = ((BigDecimal) rs.get(0).get("averageIterationLength")).intValue();
                    break;
                case 2:
                    if (rs.size() != 0) {
                        numberOfIterationsWhichContainsAtLeastOneActivityForFeedback = ((Long) rs.get(0).get("totalCountOfIterationsWithSubstringsInActivity")).intValue();
                    }
                    break;
                case 3:
                    Date activityEndDate;
                    for (Map<String, Object> map : rs) {
                        activityEndDate = (Date) map.get("endDate");
                        feedbackActivityEndDates.add(activityEndDate);
                    }
                    break;
                case 4:
                    projectStartDate = (Date) rs.get(0).get("startDate");
                    break;
                case 5:
                    projectEndDate = (Date) rs.get(0).get("endDate");
                    break;
                case 6:
                    numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback = rs.size();
                    Date wikiPageEndDate;
                    for (Map<String, Object> map : rs) {
                        wikiPageEndDate = (Date) map.get("appointmentDate");
                        feedbackWikiPagesEndDates.add(wikiPageEndDate);
                    }
                    break;
                default:

            }
        }

        double halfNumberOfIterations = totalNumberIterations * getDivisionOfIterationsWithFeedbackLoop();

        // if the number of iterations that contain at least one feedback activity is the ideal case
        if (totalNumberIterations <= numberOfIterationsWhichContainsAtLeastOneActivityForFeedback) {
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Number of iterations", Long.toString(totalNumberIterations)),
                    new ResultDetail("Number of iterations with feedback loops", Integer.toString(numberOfIterationsWhichContainsAtLeastOneActivityForFeedback)),
                    new ResultDetail("Conclusion", "In each iteration is at least one activity that represents feedback loop"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);

            // if there was contact with the customer in at least half of the iterations => check the spacing
        } else if (feedbackActivityEndDates.size() > halfNumberOfIterations) {

            Date firstDate = projectStartDate;
            Date secondDate = null;

            for (Date feedbackActivityDate : feedbackActivityEndDates) {
                secondDate = feedbackActivityDate;
                long daysBetween = Utils.daysBetween(firstDate, secondDate);
                firstDate = secondDate;

                if (daysBetween >= getMaxGapBetweenFeedbackLoopRate() * averageIterationLength) {
                    List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                            new ResultDetail("Days between", Long.toString(daysBetween)),
                            new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                            new ResultDetail("Conclusion", "Customer feedback loop is too long"));


                    return new QueryResultItem(this.antiPattern, true, resultDetails);
                }
            }

            // feedback spacing is ok
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                    new ResultDetail("Conclusion", "Customer feedback has been detected and there is not too much gap between them"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);

            // too few activities found => try to look in wiki pages
        } else {

            // if there is at least one wiki page in each iteration indicating a meeting => ideal case
            if (numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback >= totalNumberIterations) {
                List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                        new ResultDetail("Number of iterations", Long.toString(totalNumberIterations)),
                        new ResultDetail("Number of iterations with feedback loops", Integer.toString(numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback)),
                        new ResultDetail("Conclusion", "In each iteration is created/edited at least one wiki page that represents feedback loop"));

                return new QueryResultItem(this.antiPattern, false, resultDetails);
            }

            // if there was contact with the customer in at least half of the iterations => check the spacing
            Date firstDate = projectStartDate;
            Date secondDate = null;

            for (Date feedbackWikipagesDate : feedbackWikiPagesEndDates) {
                secondDate = feedbackWikipagesDate;
                long daysBetween = Utils.daysBetween(firstDate, secondDate);
                firstDate = secondDate;

                if (daysBetween >= getMaxGapBetweenFeedbackLoopRate() * averageIterationLength) {
                    List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                            new ResultDetail("Days between", Long.toString(daysBetween)),
                            new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                            new ResultDetail("Conclusion", "Customer feedback loop is too long"));


                    return new QueryResultItem(this.antiPattern, true, resultDetails);
                }
            }
            // feedback spacing is ok
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                    new ResultDetail("Conclusion", "Customer feedback has been detected and there is not too much gap between them"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }
    }
}
