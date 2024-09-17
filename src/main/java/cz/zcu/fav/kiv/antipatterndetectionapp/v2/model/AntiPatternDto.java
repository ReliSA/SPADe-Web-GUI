package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import java.util.List;

public class AntiPatternDto {

    private String antiPattern;
    private List<ThresholdDto> thresholds;

    public String getAntiPattern() {
        return antiPattern;
    }

    public void setAntiPattern(String antiPattern) {
        this.antiPattern = antiPattern;
    }

    public List<ThresholdDto> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<ThresholdDto> thresholds) {
        this.thresholds = thresholds;
    }
}
