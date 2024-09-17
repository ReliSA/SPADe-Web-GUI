package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UnknownPosterDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(UnknownPosterDetectorImpl.class);

    public final String configJsonFileName = "UnknownPoster.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_all_persons_without_full_name.sql",
            "select_identities_with_valid_information.sql"
    );

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private List<String> getSearchSubstringsInvalidNames(Map<String, String> thresholds) {
        return Arrays.asList(thresholds.get("searchSubstringsInvalidNames").split("\\|\\|"));
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
     * 1) find all persons involved in the project
     * 2) check if persons have full name filled in
     * 3) select identities for unidentified persons
     * 4) check if every person has filled in at least one of the information in one of used identities
     *    (email, full name in description, valid nickname)
     * 5) if there is at least one unidentified person, the anti-pattern is detected
     *
     * @param project model class for analyzed project
     * @param databaseConnection database connection
     * @param thresholds current thresholds
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {
        List<String> queriesFirstPart = this.sqlQueries.subList(0, 2);
        List<String> queriesSecondPart = this.sqlQueries.subList(2, 3);

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, queriesFirstPart);

        int resultNumber = 0;
        for(Map<String, Object> result : resultSets.get(0)) {
            List<String> invalidIds = new ArrayList<>();
            invalidIds.add(result.get("id").toString());

            List<String> substringsInvalidNames = getSearchSubstringsInvalidNames(thresholds);
            for(String substring : substringsInvalidNames)
                invalidIds.add(substring);

            List<List<Map<String, Object>>> resultsForInvalidIds = databaseConnection.executeQueriesWithMultipleResults(project, Utils.fillQueryWithSearchSubstrings(queriesSecondPart, invalidIds));

            if(Integer.parseInt(resultsForInvalidIds.get(0).get(0).get("count(i.id)").toString()) == 0)
                resultNumber++;
        }

        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Number of unknown contributors", String.valueOf(resultNumber)));

        if(resultNumber > 0) {
            resultDetails.add(new ResultDetail("Conclusion", "Unknown contributors were detected."));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Conclusion", "Any unknown contributors were not detected."));
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
