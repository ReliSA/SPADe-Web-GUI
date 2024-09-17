package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;

import java.util.List;

/**
 * Model class for store results of one specific projects.
 *
 * project: analyzed project
 * queryResultItems: all analyzed anti-patterns with results and results details
 */
public class QueryResult {
    private ProjectDto project;
    private List<QueryResultItem> queryResultItems;

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public List<QueryResultItem> getQueryResultItems() {
        return queryResultItems;
    }

    public void setQueryResultItems(List<QueryResultItem> queryResultItems) {
        this.queryResultItems = queryResultItems;
    }

    public QueryResult() {
    }

    public QueryResult(ProjectDto project, List<QueryResultItem> queryResultItems) {
        this.project = project;
        this.queryResultItems = queryResultItems;
    }
}
