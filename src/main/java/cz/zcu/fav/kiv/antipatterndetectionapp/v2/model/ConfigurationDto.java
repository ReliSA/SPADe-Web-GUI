package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import java.util.List;

public class ConfigurationDto {
    private List<AntiPatternDto> configuration;

    public List<AntiPatternDto> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<AntiPatternDto> configuration) {
        this.configuration = configuration;
    }
}
