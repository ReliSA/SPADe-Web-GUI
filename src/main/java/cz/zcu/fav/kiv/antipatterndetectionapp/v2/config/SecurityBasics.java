package cz.zcu.fav.kiv.antipatterndetectionapp.v2.config;

public class SecurityBasics {

    public static final String[] NO_AUTHORIZATION_NEEDED = new String[]{
            "/v2/user/register",
            "/v2/user/login",
            "/v2/user/refresh",
            "/v2/user/logout",
            "/v2/app/metadata/about"
    };

}
