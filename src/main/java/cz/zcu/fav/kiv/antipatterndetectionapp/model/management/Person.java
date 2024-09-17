package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class for people. This is Entity class that is loaded from db.
 */
@Entity
public class Person implements DatabaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="externalid")
    private String externalId;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<Identity> identities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "projectid")
    private Project project;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<WorkItem> workItems = new LinkedHashSet<>();

    public Set<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(Set<WorkItem> workItems) {
        this.workItems = workItems;
    }

    /**
     * Default constructor for Person
     */
    public Person() {}

    /**
     * Constructor for Person
     * @param name      Name of the Person
     * @param project   Project belonging to person
     */
    public Person(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    /**
     * Method adding new identity to person
     * @param identity  Identity of person
     */
    public void addIdentity(Identity identity) {
        this.identities.add(identity);
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

    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String getObjectName() {
        return "person";
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return person.getId().equals(getId());
    }

    @Override
    public String getAttributeValue(String attr) {
        if(attr.compareTo("id") == 0) {
            return getId().toString();
        } else if(attr.compareTo("name") == 0) {
            return getName();
        } else if (attr.compareTo("externalId") == 0) {
            return getExternalId();
        } else {
            return "";
        }
    }
}
