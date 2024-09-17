package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @Author Jiri Trefil
 * primary key of table user_configuration is compound (userId,configurationId) pair is unique
 * the business logic involving this table is in ConfigurationRepository interface
 */
@Embeddable
public class UserConfigKey implements Serializable {

private int userId;
private int configId;

public UserConfigKey(int userId,int configId){
    this.userId = userId;
    this.configId = configId;
}

    public int getUserId() {
        return userId;
    }

    public int getConfigId() {
        return configId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public UserConfigKey() {
        this.userId = -1;
        this.configId = -1;
    }
}
