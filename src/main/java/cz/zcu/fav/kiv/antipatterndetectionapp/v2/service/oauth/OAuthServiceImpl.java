package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Service which communicate with OAuth application
 */
@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {

    /**
     * URL path to authenticate endpoint of OAuth application
     */
//    @Value("${auth.realm.authenticate}")
    private String AUTH_URL_AUTH = "http://localhost:8081/authenticate";

    /**
     * URL path to login endpoint of OAuth application
     */
//    @Value("${auth.realm.login}")
    private String AUTH_URL_LOGIN = "http://localhost:8081/login";

    /**
     * URL path to logout endpoint of OAuth application
     */
//    @Value("${auth.realm.logout}")
    private String AUTH_URL_LOGOUT = "http://localhost:8081/logout";

    private String AUTH_URL_REFRESH = "http://localhost:8081/refresh";

    @Autowired
    private RequestBuilder requestBuilder;
    /**
     *
     */
    @Autowired
    private UserService userService;


    //private RequestBuilder requestBuilder = new RequestBuilder();

    public ResponseEntity<String> authenticate(String token) {

        return requestBuilder.sendRequestResponse(AUTH_URL_AUTH, token);
    }

    public ResponseEntity<String> loginUser(User user) {
        final String userName = user.getName();

        if(userName == null) {
            return null;
        }
        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("name", userName);

        return requestBuilder.sendRequestResponse(AUTH_URL_LOGIN, requestBody);
    }

    public ResponseEntity<String> logoutUser(User user) {
        final String userName = user.getName();
        final String token = user.getToken();

        if(userName == null || token == null) {
            return null;
        }

        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, Object> requestBody = new HashMap<>();

        requestBody.put("name", userName);
        requestBody.put("token", token);

        return requestBuilder.sendRequestResponse(AUTH_URL_LOGOUT, requestBody);
    }

    @Override
    public ResponseEntity<String> refreshToken(String token) {
        return requestBuilder.sendRequestResponse(AUTH_URL_REFRESH, token, true);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = this.userService.getUserByName(s);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),new ArrayList<>());
    }
}
