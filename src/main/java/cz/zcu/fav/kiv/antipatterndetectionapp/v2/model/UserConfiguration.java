package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

/**
 * @author Jiri Trefil
 * Wrapper for http request for uploading a new configuration by user
 * DTO pattern
 */
public class UserConfiguration {
    private User user;
    private ConfigurationDto configuration;
    private String isDefault;
    private String configurationName;
    private String id;

    public UserConfiguration(User user, ConfigurationDto configuration, String isDefault, String configurationName) {
        this.user = user;
        this.configuration = configuration;
        this.isDefault = isDefault;
        this.configurationName = configurationName;
        this.id = null;
    }
    public UserConfiguration(){}

    public UserConfiguration(User user, String id) {
        this.user = user;
        this.configuration = null;
        this.configurationName = null;
        this.isDefault = null;
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public ConfigurationDto getConfiguration() {
        return this.configuration;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setConfiguration(ConfigurationDto configuration) {
        this.configuration = configuration;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
