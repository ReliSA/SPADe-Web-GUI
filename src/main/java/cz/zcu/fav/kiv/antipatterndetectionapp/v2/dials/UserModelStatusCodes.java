package cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials;

/**
 * Enum for status codes
 */
public enum UserModelStatusCodes {
    INVALID_USER_ARGUMENTS("Invalid user arguments for registration",400),
    USER_EXISTS("User exists",400),
    HASH_FAILED("Error occurred while creating user",500),
    USER_CREATED("User successfully created",201),
    USER_LOGGED_IN("User logged in successfully",200),
    USER_LOGIN_FAILED("User login failed",401),
    USER_CREATION_FAILED("Error occurred while creating user",503),
    USER_LOGGED_OUT("User logged out.",200),
    TOKEN_REFRESHED("User token refreshed.",200),
    TOKEN_EXPIRED("User token is expired.",401);

    private final String label;
    private final int statusCode;

    UserModelStatusCodes(String s, int i) {
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
