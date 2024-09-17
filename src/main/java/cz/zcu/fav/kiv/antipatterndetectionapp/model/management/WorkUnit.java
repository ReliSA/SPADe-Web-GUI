package cz.zcu.fav.kiv.antipatterndetectionapp.model.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model class for work units. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "work_unit")
public class WorkUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "startdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp startDate;

    @Column(name = "duedate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp dueDate;

    @ManyToOne
    @JoinColumn(name = "assigneeid")
    private Person assignee;

    @ManyToOne
    @JoinColumn(name = "wutypeid")
    private WuType type;

    @ManyToOne
    @JoinColumn(name = "activityid")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "iterationid")
    private Iteration iteration;

    @ManyToOne
    @JoinColumn(name = "phaseid")
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "projectid")
    private Project project;

    @ManyToMany
    @JoinTable(name = "work_unit_category",
            joinColumns = @JoinColumn(name = "workunitid"),
            inverseJoinColumns = @JoinColumn(name = "categoryid"))
    private Set<Category> categories = new LinkedHashSet<>();

    public WuType getType() {
        return type;
    }

    public void setType(WuType type) {
        this.type = type;
    }

    public Person getAssignee() {
        return assignee;
    }

    public void setAssignee(Person assignee) {
        this.assignee = assignee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
}
