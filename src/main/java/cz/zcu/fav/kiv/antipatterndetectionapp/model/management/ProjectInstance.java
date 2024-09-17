package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class for project instances. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "project_instance")
public class ProjectInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="externalid")
    private String externalId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "projectid")
    private Project projectId;

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Priority> priorities = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Severity> severities = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Status> statuses = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Resolution> resolutions = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<WuType> wuTypes = new HashSet<>();

    @OneToMany(mappedBy = "projectInstance", orphanRemoval = true)
    private Set<Relation> relations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Set<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(Set<Priority> priorities) {
        this.priorities = priorities;
    }

    public Set<Severity> getSeverities() {
        return severities;
    }

    public void setSeverities(Set<Severity> severities) {
        this.severities = severities;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(Set<Resolution> resolutions) {
        this.resolutions = resolutions;
    }

    public Set<WuType> getWuTypes() {
        return wuTypes;
    }

    public void setWuTypes(Set<WuType> wuTypes) {
        this.wuTypes = wuTypes;
    }

    public Set<Relation> getRelations() {
        return relations;
    }

    public void setRelations(Set<Relation> relations) {
        this.relations = relations;
    }

    public Set<? extends EnumType> getEnumsByName(String enumName) {

        if(enumName.contentEquals("Priority")) {
            return getPriorities();
        } else if(enumName.contentEquals("Severity")) {
            return getSeverities();
        } else if(enumName.contentEquals("Status")) {
            return getStatuses();
        } else if(enumName.contentEquals("Role")) {
            return getRoles();
        } else if(enumName.contentEquals("Resolution")) {
            return getResolutions();
        } else if(enumName.contentEquals("WuType")) {
            return getWuTypes();
        } else if(enumName.contentEquals("Relation")) {
            return getRelations();
        } else {
            return null;
        }
    }
}
