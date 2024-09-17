package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.*;

/**
 * @author Petr Urban
 * @since 2023-04-26
 * @version 1.0.0
 */
@Entity
@Table(name = "app_metadata")
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appDataKey;

    private String appDataValue;

    public String getAppDataKey() {
        return appDataKey;
    }

    public void setAppDataKey(String appDataKey) {
        this.appDataKey = appDataKey;
    }

    public String getAppDataValue() {
        return appDataValue;
    }

    public void setAppDataValue(String appDataValue) {
        this.appDataValue = appDataValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
