package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import java.util.List;

public class CategoryChangeRequest {

    private List<CategoryDto> categories;

    private Integer submitType;

    private long projectId;

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public Integer getSubmitType() {
        return submitType;
    }

    public void setSubmitType(Integer submitType) {
        this.submitType = submitType;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
