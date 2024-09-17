package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SelectedWorkUnitsDto {
    @JsonProperty("activityId")
    private long activityId;

    @JsonProperty("wuIds")
    private List<Long> wuIds;

    // Getter and Setter methods

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public List<Long> getWuIds() {
        return wuIds;
    }

    public void setWuIds(List<Long> wuIds) {
        this.wuIds = wuIds;
    }
}
