package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for role classes. This is Entity class that is loaded from db.
 */
@Entity
public class RoleClassification implements Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="class")
    private String name;

    @Column(name="superclass")
    private String superClass;

    @OneToMany(mappedBy = "classId", orphanRemoval = true)
    private List<Role> roles = new ArrayList<>();

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

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
