package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class for project. This is Entity class that is loaded from db.
 */
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "superprojectid")
    private Project superProject;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<Person> people = new ArrayList<>();

    @OneToMany(mappedBy = "projectId", orphanRemoval = true)
    private List<ProjectInstance> projectInstances = new ArrayList<>();

    @OneToMany(mappedBy = "superProjectId", orphanRemoval = true)
    private List<Iteration> iterations = new ArrayList<>();

    @OneToMany(mappedBy = "superProjectId", orphanRemoval = true)
    private List<Phase> phases = new ArrayList<>();

    @OneToMany(mappedBy = "superProjectId", orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private Set<Configuration> configurations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<WorkUnit> workUnits = new ArrayList<>();

    public Set<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Set<Configuration> configurations) {
        this.configurations = configurations;
    }

    public Project getSuperProject() {
        return superProject;
    }

    public void setSuperProject(Project superProject) {
        this.superProject = superProject;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public List<Iteration> getIterations() {
        return iterations;
    }

    public void setIterations(List<Iteration> iterations) {
        this.iterations = iterations;
    }

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<ProjectInstance> getProjectInstances() {
        return projectInstances;
    }

    public void setProjectInstances(List<ProjectInstance> projectInstances) {
        this.projectInstances = projectInstances;
    }

    public List<WorkUnit> getWorkUnits() {
        return workUnits;
    }

    public void setWorkUnits(List<WorkUnit> workUnits) {
        this.workUnits = workUnits;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
