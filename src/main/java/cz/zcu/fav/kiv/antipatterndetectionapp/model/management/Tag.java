package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import javax.persistence.*;

/**
 * Model class for tags. This is Entity class that is loaded from db.
 */
@Entity
public class Tag {

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

    @ManyToOne
    @JoinColumn(name = "configurationid")
    private Commit configuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commit getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Commit configuration) {
        this.configuration = configuration;
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
}
