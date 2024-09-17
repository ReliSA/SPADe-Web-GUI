package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.ApplicationProperties;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.SpringApplicationContext;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that takes care of database connections and running queries.
 */
public class DatabaseConnection {
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

    private final Connection databaseConnection;

    /**
     * Constructor that takes application properties from configuration file name application.properties
     * and creating new database connection with given parameters.
     */
    public DatabaseConnection() {
        ApplicationProperties applicationProperties = ((ApplicationProperties) SpringApplicationContext.getContext()
                .getBean("applicationProperties"));
        String connectionUrl = applicationProperties.getDataSourceUrl();
        String dataSourceUsername = applicationProperties.getDataSourceUsername();
        String dataSourcePassword = applicationProperties.getDataSourcePassword();
        this.databaseConnection = createConnection(connectionUrl, dataSourceUsername, dataSourcePassword);
    }

    /**
     * Method for creating database connection with given parameters.
     *
     * @param connectionUrl      database url to connect
     * @param dataSourceUsername database username
     * @param dataSourcePassword database user password
     * @return new database connection
     */
    private Connection createConnection(String connectionUrl, String dataSourceUsername, String dataSourcePassword) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl, dataSourceUsername, dataSourcePassword);

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("Connection with DB could not be created");
        }
        return conn;
    }

    /**
     * Method for closing database connection. It is necessary to close the database
     * connection when the database connection is not used due to the accumulation
     * of unused connections.
     */
    void closeConnection() {
        try {
            this.databaseConnection.close();
        } catch (SQLException e) {
            LOGGER.error("Connection to DB could not be closed!");
            e.printStackTrace();
        }
    }

    /**
     * Simple getter for getting the database connection.
     *
     * @return database connection
     */
    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    /**
     * Method for analyzing a given project. These methods are intended
     * for queries that have a single select output (for example Road To Nowhere).
     *
     * @param project analyzed project
     * @param queries list of queries
     * @return result set of results
     */
    public ResultSet executeQueries(Project project, List<String> queries) {
        ResultSet resultSet = null;
        try (Statement stmt = this.getDatabaseConnection().createStatement()) {

            for (String query : queries) {
                if (queries.indexOf(query) != queries.size() - 1) {
                    if (query.contains("?"))
                        query = query.replace("?", project.getId().toString());
                    stmt.executeQuery(query);
                } else {
                    resultSet = stmt.executeQuery(query);
                }

            }
        } catch (SQLException e) {
            LOGGER.error("DB query could not be performed!");
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * The method sends several queries to the database and generates map results from them.
     * @param project analyzed project
     * @param queries list of queries
     * @return object of results
     */
    public List<List<Map<String, Object>>> executeQueriesWithMultipleResults(Project project, List<String> queries) {
        List<List<Map<String, Object>>> allResults = new ArrayList<>();
        ResultSet resultSet;
        try(Statement stmt = this.getDatabaseConnection().createStatement()) {

            for (String query : queries) {
                if (queries.indexOf(query) != queries.size() - 1) {
                    if (query.contains("?")) {
                        query = query.replace("?", project.getId().toString());
                    }
                }
                resultSet = stmt.executeQuery(query);

                if (query.toLowerCase().startsWith("select")) {
                    allResults.add(Utils.resultSetToArrayList(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("DB query with multiple results could not be performed!");
            e.printStackTrace();
        }

        return allResults;
    }
}
