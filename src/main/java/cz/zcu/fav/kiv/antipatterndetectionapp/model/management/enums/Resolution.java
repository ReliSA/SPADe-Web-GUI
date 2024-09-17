package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;

import javax.persistence.*;

/**
 * Model class for resolution. This is Entity class that is loaded from db.
 */
@Entity
public class Resolution implements EnumType {

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
    @JoinColumn(name = "classid")
    private ResolutionClassification classId;

    @ManyToOne
    @JoinColumn(name = "projectinstanceid")
    private ProjectInstance projectInstance;

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

    public ResolutionClassification getClassId() {
        return classId;
    }

    @Override
    public void setClassId(Classification classId) {
        setClassId((ResolutionClassification) classId);
    }

    public void setClassId(ResolutionClassification classId) {
        this.classId = classId;
    }

    public ProjectInstance getProjectInstance() {
        return projectInstance;
    }

    public void setProjectInstance(ProjectInstance projectInstance) {
        this.projectInstance = projectInstance;
    }
}
