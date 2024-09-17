package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model class for commit. This is Entity class that is loaded from db.
 */
@Entity
public class Commit {

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private CommittedConfiguration committedConfiguration;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Configuration configuration;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private WorkItem workItem;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isrelease", nullable = false)
    private boolean isRelease;

    @Column(name = "identifier", nullable = true)
    private String identifier;

    @ManyToMany
    @JoinTable(name = "configuration_branch",
            joinColumns = @JoinColumn(name = "configurationid"),
            inverseJoinColumns = @JoinColumn(name = "branchid"))
    private Set<Branch> branches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "configuration", orphanRemoval = true)
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "commit", orphanRemoval = true)
    private Set<ConfigurationPersonRelation> personRelation = new LinkedHashSet<>();

    public Set<ConfigurationPersonRelation> getPersonRelation() {
        return personRelation;
    }

    public void setPersonRelation(Set<ConfigurationPersonRelation> personRelation) {
        this.personRelation = personRelation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public void setRelease(boolean release) {
        isRelease = release;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public CommittedConfiguration getCommittedConfiguration() {
        return committedConfiguration;
    }

    public void setCommittedConfiguration(CommittedConfiguration committedConfiguration) {
        this.committedConfiguration = committedConfiguration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public WorkItem getWorkItem() {
        return workItem;
    }

    public void setWorkItem(WorkItem workItem) {
        this.workItem = workItem;
    }
}
