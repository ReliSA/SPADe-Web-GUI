package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import javax.persistence.*;

/**
 * Model class for configurations. This is Entity class that is loaded from db.
 */
@Entity
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projectid")
    private Project project;

    @OneToOne(mappedBy = "configuration")
    private CommittedConfiguration committedConfiguration;

    @OneToOne(mappedBy = "configuration")
    private Commit commit;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommittedConfiguration getCommittedConfiguration() {
        return committedConfiguration;
    }

    public void setCommittedConfiguration(CommittedConfiguration committedConfiguration) {
        this.committedConfiguration = committedConfiguration;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
