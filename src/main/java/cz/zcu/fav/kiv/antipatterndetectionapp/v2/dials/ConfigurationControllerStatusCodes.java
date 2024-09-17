package cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials;

/**
 * enum codes for configuration service
 * this enum is used by configuration controller for the purpose of building response entities
 */
public enum ConfigurationControllerStatusCodes {

    EMPTY_CONFIGURATION_DEFINITION("No configuration definition provided",400),
    EMPTY_CONFIGURATION_NAME("No configuration name provided",400),

    CONFIGURATION_IS_DEFAULT("Default configurations can not be changed", 400),

    USER_DONT_HAVE_RIGHTS_TO_CHANGE_CONFIGURATION("You don't have rights to change configuration", 403 ),

    INSERT_FAILED("Failed to save configuration",500),

    INSERT_SUCCESSFUL("Configuration saved",201),
    UPDATE_SUCCESSFUL("Configuration updated",200),

    CONFIGURATION_PAIRING_EXISTS("Configuration already exists in your collection",400),
    CONFIGURATION_PAIRING_CREATED("Configuration added to collection",200);




    private final String label;
    private final int statusCode;

    ConfigurationControllerStatusCodes(String s, int i) {
        this.label = s;
        this.statusCode = i;
    }

    public int getStatusCode(){
        return this.statusCode;
    }
    public String getLabel(){
        return this.label;
    }

}
