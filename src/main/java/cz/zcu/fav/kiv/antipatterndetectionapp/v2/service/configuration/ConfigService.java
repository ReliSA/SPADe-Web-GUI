package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.ConfigurationControllerStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfiguration;

import java.util.List;
import java.util.Map;

public interface ConfigService {
    //upload configuration
    ConfigurationControllerStatusCodes addConfiguration(UserConfiguration cfg);
    ConfigurationControllerStatusCodes updateConfiguration(UserConfiguration cfg);

    ConfigurationControllerStatusCodes pairConfigurationWithUser(User user, Configuration configuration);


    List<Configuration> getUserConfigurations(User user);

    Configuration getConfigurationById(int id);

    Configuration getConfiguration(String userName, int id);
    String getConfigurationName(int userId, int configurationId);

    Map<String, AntiPattern> getAntiPatterns();
}
