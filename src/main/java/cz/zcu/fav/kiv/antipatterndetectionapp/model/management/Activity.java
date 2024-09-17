package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for category. This is Entity class that is loaded from db.
 */
@Entity
public class Activity implements DatabaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="externalid")
    private String externalId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="startdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp startDate;

    @Column(name="enddate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "superprojectid")
    private Project superProjectId;

    @OneToMany(mappedBy = "activity", orphanRemoval = true)
    private List<WorkUnit> workUnits = new ArrayList<>();

    /**
     * Default constructor for Activity
     */
    public Activity() {}

    /**
     * Constructor for new Activity
     * @param externalId    External id from ALM tool
     * @param name          Name of the activity
     * @param description   Description of the activity
     */
    public Activity(String externalId, String name, String description) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor for new activity with project relation
     * @param externalId        External id from ALM tool
     * @param name              Name of the activity
     * @param description       Description of the activity
     * @param superProjectId    Project belonging to activity
     */
    public Activity(String externalId, String name, String description, Project superProjectId) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.superProjectId = superProjectId;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Project getSuperProjectId() {
        return superProjectId;
    }

    public void setSuperProjectId(Project superProjectId) {
        this.superProjectId = superProjectId;
    }

    public List<WorkUnit> getWorkUnits() {
        return workUnits;
    }

    public void setWorkUnits(List<WorkUnit> workUnits) {
        this.workUnits = workUnits;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getObjectName() {
        return "activity";
    }

    @Override
    public String getAttributeValue(String attr) {
        if(attr.compareTo("id") == 0) {
            return getId().toString();
        } else if(attr.compareTo("name") == 0) {
            return getName();
        } else if (attr.compareTo("description") == 0) {
            return getDescription();
        } else if (attr.compareTo("externalId") == 0) {
            return getExternalId();
        } else {
            return "";
        }
    }
}
