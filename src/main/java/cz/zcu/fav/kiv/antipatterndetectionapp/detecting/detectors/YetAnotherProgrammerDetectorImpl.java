package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class YetAnotherProgrammerDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(YetAnotherProgrammerDetectorImpl.class);

    public final String configJsonFileName = "YetAnotherProgrammer.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_project_start_date.sql",
            "select_project_end_date.sql",
            "select_persons_in_the_project.sql",
            "select_first_contribution_of_person.sql"
    );

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMaximumNumberOfNewContributors(Map<String, String> thresholds) {
        return Integer.parseInt(thresholds.get("maxNumberOfNewContributors"));
    }

    private int getNumberOfFirstMonthsWithoutDetection(Map<String, String> thresholds) {
        return Integer.parseInt(thresholds.get("numberOfFirstMonthsWithoutDetection"));
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
     * 1) select start and end date of the project
     * 2) select all persons involved in the project
     * 3) get first involvement date for each person in the project
     * 4) divide project into months
     * 5) for each month (except first X months) get number of persons with first involvement in this month
     * 6) if number of new persons in any month is more than Y, the anti-pattern is detected
     *
     * @param project model class for analyzed project
     * @param databaseConnection database connection
     * @param thresholds current thresholds
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {
        List<String> queriesFirstPart = this.sqlQueries.subList(0, 4);
        List<String> queriesSecondPart = this.sqlQueries.subList(4, 5);

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, queriesFirstPart);

        // Getting start and end dates of the project
        LocalDateTime startDateTime= LocalDate.parse(resultSets.get(0).get(0).get("startDate").toString()).atStartOfDay();
        LocalDateTime endDateTime= LocalDate.parse(resultSets.get(1).get(0).get("endDate").toString()).atStartOfDay();

        //System.out.println("Project: " + project.getId() + " - Start: " + startDateTime + " - End: " + endDateTime);

        // Map with contributors and their first time involvement date
        Map<String, LocalDateTime> contributors = new HashMap<>();

        for(Map<String, Object> result : resultSets.get(2)) {
            // Getting first time involvement date for id of person
            List<String> parameters = new ArrayList<>();
            parameters.add(result.get("id").toString());
            List<List<Map<String, Object>>> resultsForInvalidIds = databaseConnection.executeQueriesWithMultipleResults(project, Utils.fillQueryWithSearchSubstrings(queriesSecondPart, parameters));

            // Putting first time involvement date in the map
            for(Map<String, Object> res : resultsForInvalidIds.get(0)) {
                LocalDateTime dateTime = LocalDateTime.parse(res.get("created").toString());
                contributors.put(result.get("id").toString(), dateTime);
                //System.out.println("- Person: " + result.get("id").toString() + " - " + dateTime);
            }
        }

        // Retrieving number of first time contributors in certain period (month)
        LocalDateTime tmpPeriodStart = startDateTime;
        LocalDateTime tmpPeriodEnd;

        tmpPeriodStart = startDateTime.plusMonths(getNumberOfFirstMonthsWithoutDetection(thresholds));
        tmpPeriodEnd = tmpPeriodStart.plusMonths(1);

        boolean detected = false;
        int detectedMonthsNumber = 0;
        int numberOfNewContributors;

        while(tmpPeriodStart.isBefore(endDateTime)){
            numberOfNewContributors = 0;
            for(String key : contributors.keySet()){
                LocalDateTime tmp = contributors.get(key);
                if((tmp.isAfter(tmpPeriodStart) || tmp.isEqual(tmpPeriodStart)) && tmp.isBefore(tmpPeriodEnd))
                    numberOfNewContributors++;
            }
            if(numberOfNewContributors > getMaximumNumberOfNewContributors(thresholds)) {
                detected = true;
                detectedMonthsNumber++;
            }

            //System.out.println("Project: "+ project.getId() + " - Period: " + tmpPeriodStart + " - " + tmpPeriodEnd + "- Number of new contributors: " + numberOfNewContributors);

            tmpPeriodStart = tmpPeriodStart.plusMonths(1);
            tmpPeriodEnd = tmpPeriodStart.plusMonths(1);
        }

        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Months number with detected increase", String.valueOf(detectedMonthsNumber)));
        if(detected == true)
            resultDetails.add(new ResultDetail("Conclusion", "Significant increase of new contributors at a later stage of the project detected"));
        else
            resultDetails.add(new ResultDetail("Conclusion", "No significant increase of new contributors at a later stage of the project detected"));

        return new QueryResultItem(this.antiPattern, detected, resultDetails);
    }
}
