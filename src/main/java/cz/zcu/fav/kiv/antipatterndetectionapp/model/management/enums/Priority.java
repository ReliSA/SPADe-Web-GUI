package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;

import javax.persistence.*;

/**
 * Model class for priority. This is Entity class that is loaded from db.
 */
@Entity
public class Priority implements EnumType {

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
    private PriorityClassification classId;

    @ManyToOne
    @JoinColumn(name = "projectinstanceid")
    private ProjectInstance projectInstance;

    public Priority() {}

    public Priority(String externalId, String name, String description) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityClassification getClassId() {
        return classId;
    }

    @Override
    public void setClassId(Classification classId) {
        setClassId((PriorityClassification) classId);
    }

    public void setClassId(PriorityClassification classId) {
        this.classId = classId;
    }

    public ProjectInstance getProjectInstance() {
        return projectInstance;
    }

    public void setProjectInstance(ProjectInstance projectInstance) {
        this.projectInstance = projectInstance;
    }
}
