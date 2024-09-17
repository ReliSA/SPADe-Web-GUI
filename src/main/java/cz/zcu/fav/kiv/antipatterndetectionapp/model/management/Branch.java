package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model class for branches. This is Entity class that is loaded from db.
 */
@Entity
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="externalid")
    private String externalId;

    @Column(name="name")
    private String name;

    @Column(name="ismain")
    private boolean isMain;

    @ManyToMany
    @JoinTable(name = "configuration_branch",
            joinColumns = @JoinColumn(name = "branchid"),
            inverseJoinColumns = @JoinColumn(name = "configurationid"))
    private Set<Commit> commits = new LinkedHashSet<>();

    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

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

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}
