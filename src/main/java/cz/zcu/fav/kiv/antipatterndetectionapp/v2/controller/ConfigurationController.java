package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.ConfigurationControllerStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration.ConfigService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jiri Trefil
 * Controller providing API for configuration management
 */
@RestController
@RequestMapping("v2/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigService configurationService;

    /**
     * Endpoint for user to save his custom configuration
     * @param userConfiguration Wrapper around http request - contains User and Configuration objects
     *                          which point to the user uploading the configuration and the definition of his configuration
     * @return ResponseEntity with json body  appropriate status code indicating result of the operation
     */
    @PostMapping(value="/upload_configuration")
    public ResponseEntity<String> uploadConfiguration(@RequestBody UserConfiguration userConfiguration) {
        ConfigurationControllerStatusCodes returnCode = configurationService.addConfiguration(userConfiguration);
        String message = returnCode.getLabel();
        Map<String,Object> json = new HashMap<>();
        json.put("message", message);
        json.put("id", userConfiguration.getId());
        //ResponseEntity<String> response = configurationService.addConfiguration(userConfiguration);
        return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.valueOf(returnCode.getStatusCode()));
    }

    /**
     * Method updates existing configuration
     * @param userConfiguration wrapper around client request for update - same as for inserting a new configuration
     * @return ResponseEntity with status code and message informing client about result of the update operation
     */
    @PutMapping(value="/update_configuration")
    public ResponseEntity<String> updateConfiguration(@RequestBody UserConfiguration userConfiguration) {
        ConfigurationControllerStatusCodes returnCode = configurationService.updateConfiguration(userConfiguration);
        String message = returnCode.getLabel();
        Map<String,Object> json = new HashMap<>();
        json.put("message",message);
        //ResponseEntity<String> response = configurationService.addConfiguration(userConfiguration);
        return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.valueOf(returnCode.getStatusCode()));
    }

    /**
     * Endpoint for accessing anti-patterns and configurations
     * @param userConfiguration Wrapper around client request body
     * @return ResponseEntity with anti-pattern array and configuration array if everything went well
     *  ResponseEntity with error status code otherwise
     */
    @PostMapping(value="/configuration")
    public ResponseEntity<String> getConfiguration(@RequestBody UserConfiguration userConfiguration) {
        Map<String, Object> json = new HashMap<>();

        Configuration config = this.configurationService.getConfiguration(userConfiguration.getUser().getName(), Integer.parseInt(userConfiguration.getId()));

        Map<String, AntiPattern> antiPatterns = this.configurationService.getAntiPatterns();

        if (config == null) {
            json.put("message", "no configurations found");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.BAD_REQUEST);
        }

        if (antiPatterns == null) {
            json.put("message", "no anti patterns found");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.NOT_FOUND);
        }

        json.put("configuration", config.getConfig());
        json.put("antiPatterns", antiPatterns);

        return new ResponseEntity<>(new Gson().toJson(json),HttpStatus.OK);

    }

    /**
     * Endpoint which retrieves configuration names and their ids
     * @param user wrapper around username send in body of the request
     * @return ResponseEntity with json body  appropriate status code indicating result of the operation
     */
    @PostMapping(value="/configuration_name")
    public ResponseEntity<String> getConfigurationNames(@RequestBody User user) {
        Map<String, Object> json = new HashMap<>();
        List<Configuration> configuration = this.configurationService.getUserConfigurations(user);
        //this can only happen if db server is offline
        //or no default configuration exists in database
        if(configuration == null) {
            json.put("message", "internal sever error");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<String> configurationNames = new ArrayList<>();
        List<Integer> configurationIds = new ArrayList<>();
        for(Configuration o : configuration){
            configurationNames.add(o.getIsDefault().equals("Y") ? o.getDefaultConfigName() : o.getConfigurationName());
            configurationIds.add(o.getId());
        }

        Map<String, Object> toJsonString = new HashMap<>();
        toJsonString.put("configuration_names", configurationNames);
        toJsonString.put("configuration_ids", configurationIds);

        return new ResponseEntity<>(new Gson().toJson(toJsonString), HttpStatus.OK);
    }



}
