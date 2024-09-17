package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;

import javax.persistence.*;

/**
 * Model class for identities. This is Entity class that is loaded from db.
 */
@Entity
public class Identity implements DatabaseObject {

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

    @Column(name="email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "personid")
    private Person person;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String getObjectName() {
        return "identity";
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
        } else if (attr.compareTo("email") == 0) {
            return getEmail();
        } else if (attr.compareTo("personId") == 0) {
            return getPerson().toString();
        } else {
            return "";
        }
    }
}
