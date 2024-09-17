package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Model class for workitems. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "work_item")
public class WorkItem {

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

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "authorid")
    private Person author;

    @OneToOne(mappedBy = "workItem")
    private Commit commit;

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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
