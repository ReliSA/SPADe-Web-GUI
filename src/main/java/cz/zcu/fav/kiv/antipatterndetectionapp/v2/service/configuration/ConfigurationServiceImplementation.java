package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.ConfigurationControllerStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationJoinRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationServiceImplementation implements ConfigService {
    //repository which represents connection to database and the configuration table in particular
    @Autowired
    private ConfigRepository configurationRepository;
    //repository for Join table, necessary to add associations between users and configurations
    @Autowired
    private UserConfigurationJoinRepository userConfigurationJoinRepository;

    @Autowired
    private AntiPatternRepository antiPatternRepository;

    private String[] configJsonFileNames = {"BusinessAsUsual.json", "BystanderApathy.json", "LongOrNonExistentFeedbackLoops.json",
            "NinetyNinetyRule.json", "RoadToNowhere.json", "SpecifyNothing.json", "TooLongSprint.json", "UnknownPoster.json",
            "VaryingSprintLength.json", "YetAnotherProgrammer.json"};


    //user service is also necessary for retrieving information about users (primarily database query for fetching id)
    @Autowired
    private UserService userService;

    /**
     * Saves configuration to database
     * the configuration is available to the user who sent the request
     * @param cfg Configuration - JSON format of configuration with antipatterns and thresholds
     * @return ResponseEntity with status code and message with more information about operation
     */
    @Override
    public ConfigurationControllerStatusCodes addConfiguration(UserConfiguration cfg) {
        Configuration configuration = parseUserConfiguration(cfg);
        //if the request is missing the configuration definition then we kill it
        if (configuration == null) {
            return ConfigurationControllerStatusCodes.EMPTY_CONFIGURATION_DEFINITION;
        }
        if (isConfigurationNameInvalid(cfg)) {
            return ConfigurationControllerStatusCodes.EMPTY_CONFIGURATION_NAME;
        }
        //fetch the user from db because user in UserConfiguration does not contain id
        User user = this.getUser(cfg.getUser());

        //create the hash of the configuration (w/o salting)
        String configHash = createConfigurationHash(configuration);
        Configuration existingConfiguration = this.configurationRepository.findConfigurationByConfigHash(configHash);
        //configuration definition does not exist => upload the configuration into database
        if (existingConfiguration == null) {
            configuration.setHash(configHash);
            //save the configuration itself
            this.configurationRepository.save(configuration);
            //can only happen if db server fails or a constraint is breached
            if (configuration == null) {
                return ConfigurationControllerStatusCodes.INSERT_FAILED;
            }
        }
        //already exists, update pointer to instance with id
        else {
            configuration = existingConfiguration;
        }

        cfg.setId(String.valueOf(configuration.getId()));

        //pair the configuration to the user
        return pairConfigurationWithUser(user, configuration);
    }

    /**
     * Wrapper around user service for fetching user from database by username
     * @param user Userdto with username only
     * @return User from db or null if user does not exist
     */

    private User getUser(User user) {
        String userName = user.getName();
        //fetch the user from db because user in UserConfiguration does not contain id
        return this.userService.getUserByName(userName);
    }

    /**
     * Quick verification if configuration name is empty
     * @param cfg dto wrapper around user update / insert configuration request
     * @return true if configuration name is empty or empty string
     */
    private boolean isConfigurationNameInvalid(UserConfiguration cfg) {
        return cfg.getConfigurationName() == null || cfg.getConfigurationName().equals("");
    }

    /**
     * Util function for creating hash of configuration definition
     * @param configuration Configuration instance with configuration definition as string
     * @return String hash created from configuration definition
     *         empty string if configuration definition is null
     */
    private String createConfigurationHash(Configuration configuration) {
        if(configuration == null) {return "";}
        return Crypto.hashString(configuration.getConfig());
    }

    /**
     * Method updates existing non default user configuration
     * the user in request must have the rights to access the configuration, ie entry userId,configId must exist in join table
     * @param cfg UserConfiguration wrapper around update request
     * @return enum with http code and message informing about the result of the operation
     */
    @Override
    public ConfigurationControllerStatusCodes updateConfiguration(UserConfiguration cfg) {
        Configuration configuration = parseUserConfiguration(cfg);
        //if the request is missing the configuration definition then we kill it
        if (configuration == null) {
            return ConfigurationControllerStatusCodes.EMPTY_CONFIGURATION_DEFINITION;
        }

        if (isConfigurationNameInvalid(cfg)) {
            return ConfigurationControllerStatusCodes.EMPTY_CONFIGURATION_NAME;
        }
        int configurationId = Integer.parseInt(cfg.getConfigurationName());
        Configuration oldConfiguration = this.getConfiguration(cfg.getUser().getName(), configurationId);

        if (oldConfiguration == null) {
            return ConfigurationControllerStatusCodes.USER_DONT_HAVE_RIGHTS_TO_CHANGE_CONFIGURATION;
        }

        if (oldConfiguration.getIsDefault().equals("Y")) {
            return ConfigurationControllerStatusCodes.CONFIGURATION_IS_DEFAULT;
        }
        String updatedConfigHash = this.createConfigurationHash(configuration);
        int configuration1 = this.configurationRepository.updateHashAndConfigurationDefinition(configurationId,configuration.getConfig(),updatedConfigHash);

        if (configuration1 == 0) {
            return ConfigurationControllerStatusCodes.INSERT_FAILED;
        }

        return ConfigurationControllerStatusCodes.UPDATE_SUCCESSFUL;
    }

    /**
     * Parser for user request wrapped around configuration
     * @param cfg UserConfiguration instance - wrapper around user request
     * @return Configuration or Null - null if the request wrapper does not containt json defition of antipatterns
     *         Instace of Configuration if the request is Okay
     */
    private Configuration parseUserConfiguration(UserConfiguration cfg){
        ConfigurationDto configurationDto = cfg.getConfiguration();
        String configurationDefinition = new Gson().toJson(configurationDto);
        String userConfigurationName = cfg.getConfigurationName();
        String defaultConfigName = null;
        if (configurationDefinition == null) {
            return null;
        }
        if (userConfigurationName == null) {
            defaultConfigName = "ahoj_svete";
        }

        return new Configuration(configurationDefinition,null,cfg.getIsDefault(),userConfigurationName,defaultConfigName);
    }

    /**
     * This method saves user and configuration id into join table
     * creates association between user and configuration in the sense of: "User @param user owns configuration @param configuration"
     * (Multiple users can own the same configuration but the configuration is not public)
     * @param user User - user who will be associated with configuration @param configuration.
     * @param configuration Configuration - the configuration that will be associated with user (just the id is necessary)
     * @return ResponseEntity<String> - Http response with status code and message about the operation
     */
    @Override
    public ConfigurationControllerStatusCodes pairConfigurationWithUser(User user, Configuration configuration) {
        final UserConfigKey key = new UserConfigKey(user.getId(),configuration.getId());
        boolean exists = this.userConfigurationJoinRepository.existsById(key);
        //the configuration pairing already exists, we do not have to do anything
        //request like this should not happen from client, something fishy might be going on
        //or the request is a duplicate
        if (exists) {
            return ConfigurationControllerStatusCodes.CONFIGURATION_PAIRING_EXISTS;
        }
        //save the relation between user and configuration
        this.userConfigurationJoinRepository.save(new UserConfigurationJoin(key,configuration.getConfigurationName()));
        return ConfigurationControllerStatusCodes.INSERT_SUCCESSFUL;
    }


    /**
     * Method queries db to retrieve all configurations available to @param user
     * if he/she does not have any of his/her (inclusive for netflix) only default configurations are returned
     * @param user user querying configuration - ONLY username is valid in this object
     * @return List<Configuration> list of available configurations
     */
    @Override
    public List<Configuration> getUserConfigurations(User user) {
        final String userName = user.getName();
        if (userName == null) {
            return null;
        }
        //client can only send his name - he obviously does not know his id in db, we have to query that
        User userInfo = this.userService.getUserByName(userName);
        //fetch all configurations this particular user can see
        //ie all public configs + configurations uploaded by this particular user
        List<Object[]> configurations = this.configurationRepository.getAllUserConfigurations(userInfo.getId());
        List<Configuration> configurationList = new ArrayList<>();
        for (Object[] o : configurations) {
            Configuration cof = (Configuration) (o[0]);
            UserConfigurationJoin cofJoin = (UserConfigurationJoin) (o[1]);
            if (cofJoin != null) {
                cof.setConfigurationName(cofJoin.getConfigurationName());
            }
            configurationList.add(cof);
        }
        return configurationList;
    }

    /**
     * finds configuration by id
     * @param id integer id of configuration to be retrieved
     */
    @Override
    public Configuration getConfigurationById(int id) {
        return this.configurationRepository.findConfigurationById(id);
    }

    /**
     * Method fetches configuration with id @param id from database
     * @param userName String username
     * @param id int configuration id
     * @return Configuration if configuration with id @param id exists and user @param userName has access to it
     *          null otherwise
     */
    @Override
    public Configuration getConfiguration(String userName, int id) {
        User user = this.userService.getUserByName(userName);
        if (user == null) {
            return null;
        }
        UserConfigKey key = new UserConfigKey(user.getId(), id);
        Configuration configuration = this.configurationRepository.findConfigurationByUserNameAndID(key,key.getConfigId());
        return configuration;
    }

    @Override
    public String getConfigurationName(int userId, int configurationId) {
        return this.configurationRepository.findConfigurationByCompoundKey(new UserConfigKey(userId, configurationId));
    }

    /**
     * Method loads antipatterns from json files on file system for metadata about anti patterns
     * @return Map of antipatterns
     */
    @Override
    public Map<String, AntiPattern> getAntiPatterns() {
        Map<String, AntiPattern> antiPatterns = new HashMap<>();
        AntiPattern tmp = null;
        for (int i = 0; i < this.configJsonFileNames.length; i++) {
            tmp = this.antiPatternRepository.getAntiPatternFromJsonFile(configJsonFileNames[i]);
            antiPatterns.put(tmp.getName(), tmp);
        }
        return antiPatterns;
    }
}
