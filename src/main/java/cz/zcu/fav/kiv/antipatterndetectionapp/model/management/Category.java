package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model class for category. This is Entity class that is loaded from db.
 */
@Entity
public class Category implements DatabaseObject {

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
    @JoinColumn(name = "projectinstanceid")
    private ProjectInstance projectInstance;

    @ManyToMany(mappedBy = "categories")
    private Set<WorkUnit> workUnits = new LinkedHashSet<>();

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

    public ProjectInstance getProjectInstance() {
        return projectInstance;
    }

    public void setProjectInstance(ProjectInstance projectInstance) {
        this.projectInstance = projectInstance;
    }

    public Set<WorkUnit> getWorkUnits() {
        return workUnits;
    }

    public void setWorkUnits(Set<WorkUnit> workUnits) {
        this.workUnits = workUnits;
    }

    @Override
    public String getObjectName() {
        return "category";
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

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Category)) {
            return false;
        }
        Category category = (Category) o;
        return category.getId().equals(getId());
    }
}
