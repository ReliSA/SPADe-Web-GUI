package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for priority classes. This is Entity class that is loaded from db.
 */
@Entity
public class PriorityClassification implements Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="class")
    private String name;

    @Column(name="superclass")
    private String superClass;

    @OneToMany(mappedBy = "classId", orphanRemoval = true)
    private List<Priority> priorities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setClassification(String classification) {
        this.name = classification;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Priority> priorities) {
        this.priorities = priorities;
    }
}
