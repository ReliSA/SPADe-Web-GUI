package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for work unit types classes. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "wu_type_classification")
public class WuTypeClassification implements Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="class")
    private String name;

    @OneToMany(mappedBy = "classId", orphanRemoval = true)
    private List<WuType> wuTypes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSuperClass() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WuType> getWuTypes() {
        return wuTypes;
    }

    public void setWuTypes(List<WuType> wuTypes) {
        this.wuTypes = wuTypes;
    }
}
