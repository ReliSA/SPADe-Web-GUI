package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface OAuthService {
    public ResponseEntity<String> authenticate(String token);
    public ResponseEntity<String> loginUser(User user);
    public ResponseEntity<String> logoutUser(User user);

    ResponseEntity<String> refreshToken(String token);

}
