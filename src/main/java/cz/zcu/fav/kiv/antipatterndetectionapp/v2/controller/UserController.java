package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Controller for providing basic support for user management
 *
 * @author Vaclav Hrabik, Jiri Trefil
 * @version 1.0
 */
@RestController
@RequestMapping("v2/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Service for work with user management
     */
    @Autowired
    private UserService userService;

    /**
     * Service for login, logout and authenticate
     */
    @Autowired
    private OAuthService aOuthService;

    /**
     * Method to register new user in the app
     *
     * @param user User who wants register in to the app
     * @return message
     */
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        UserModelStatusCodes statusCode = userService.registerUser(user);
        return getResponseEntity(statusCode, null);
    }

    /**
     * Method to login user in to the app
     *
     * @param user User who wants to login in to the app
     * @return message
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        UserModelStatusCodes statusCode = userService.verifyUser(user);
//        ResponseEntity<String> response = aOuthService.loginUser(user);

        String jwtToken = null;
        if (statusCode.getStatusCode() == 200) {
            ResponseEntity<String> response = aOuthService.loginUser(user);
            jwtToken = response.getBody();
        }

        return getResponseEntity(statusCode, jwtToken);
    }

    /**
     * Method to logout user from the app
     *
     * @param user User who wants logout from the app
     * @return message after logout
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
        final String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            //chyba
        }
        final String token = authHeader.substring(7);
        user.setToken(token);
        return aOuthService.logoutUser(user);
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader HttpHeaders headers) {
        final String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return getResponseEntity(UserModelStatusCodes.USER_LOGIN_FAILED, UserModelStatusCodes.USER_LOGIN_FAILED.getLabel());
        }
        final String token = authHeader.substring(7);

        String jwtToken = null;
        ResponseEntity<String> response = aOuthService.refreshToken(token);

        if (response.getStatusCode().is2xxSuccessful()) {
            jwtToken = response.getBody();
            return getResponseEntity(UserModelStatusCodes.TOKEN_REFRESHED, jwtToken);
        }

        return getResponseEntity(UserModelStatusCodes.TOKEN_EXPIRED, UserModelStatusCodes.TOKEN_EXPIRED.getLabel());

    }

    /**
     * Method to create response
     *
     * @param statusCode UserModelStatusCodes code
     * @return ResponseEntity with code and msg
     */
    private ResponseEntity<String> getResponseEntity(UserModelStatusCodes statusCode, String jwtToken) {
        String message = this.generateResponseObject(statusCode, jwtToken);
        int code = statusCode.getStatusCode();
        return new ResponseEntity<>(message, HttpStatus.valueOf(code));
    }

    /**
     * Method to create JSON object
     *
     * @param code UserModelStatusCodes code
     * @return String that represents JSON object
     */
    private String generateResponseObject(UserModelStatusCodes code, String jwtToken) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("message", code.getLabel());
        if (jwtToken != null) {
            json.put("jwtToken", jwtToken);
        }
        return JSONBuilder.buildJSON(json);
    }


}
