package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Model class for configurations. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "committed_configuration")
public class CommittedConfiguration {

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Configuration configuration;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "committed")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp committed;

    @OneToOne(mappedBy = "committedConfiguration")
    private Commit commit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCommitted() {
        return committed;
    }

    public void setCommitted(Timestamp committed) {
        this.committed = committed;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
