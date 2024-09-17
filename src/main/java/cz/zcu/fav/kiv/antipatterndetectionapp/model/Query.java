package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;

import java.util.List;

/**
 * Model class for query.
 */
public class Query {
    private List<ProjectDto> projects;
    private List<AntiPattern> antiPatterns;

    public Query(List<ProjectDto> projects, List<AntiPattern> antiPatterns) {
        this.projects = projects;
        this.antiPatterns = antiPatterns;
    }

    public List<ProjectDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDto> projects) {
        this.projects = projects;
    }

    public List<AntiPattern> getAntiPatterns() {
        return antiPatterns;
    }

    public void setAntiPatterns(List<AntiPattern> antiPatterns) {
        this.antiPatterns = antiPatterns;
    }
}
