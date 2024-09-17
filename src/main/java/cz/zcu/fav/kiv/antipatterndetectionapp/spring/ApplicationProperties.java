package cz.zcu.fav.kiv.antipatterndetectionapp.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Autowired
    private Environment environment;

    public String getDataSourceUrl () {
        return environment.getProperty("spring.datasource.jdbc-url");
    }

    public String getDataSourceUsername() {
        return environment.getProperty("spring.datasource.username");
    }

    public String getDataSourcePassword() {
        return environment.getProperty("spring.datasource.password");
    }

    public String getAccountUserName() {
        return environment.getProperty("account.user.name");
    }

    public String getAccountUserPassword() {
        return environment.getProperty("account.user.password");
    }
}
