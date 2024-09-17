package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import java.util.List;

/**
 * Wrapper for the merge of the persons request
 */
public class PersonMergeRequest {

    private List<PersonDto> persons;

    private String newPersonName;

    private PersonDto person;

    private long projectId;

    public List<PersonDto> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDto> persons) {
        this.persons = persons;
    }

    public String getNewPersonName() {
        return newPersonName;
    }

    public void setNewPersonName(String newPersonName) {
        this.newPersonName = newPersonName;
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
