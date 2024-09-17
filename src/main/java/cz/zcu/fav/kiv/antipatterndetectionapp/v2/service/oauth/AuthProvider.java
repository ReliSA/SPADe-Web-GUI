package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthProvider implements AuthenticationProvider {

    @Value("${auth.realm.authenticate}")
    private String auth_url;

    private RestTemplate template;

    public AuthProvider(RestTemplate restTemplate){
        this.template = restTemplate;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RestTemplate restTemplate = new RestTemplate();
        String jwtToken = (String) authentication.getCredentials();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserDetails> responseEntity = restTemplate.exchange(
                    auth_url,
                    HttpMethod.POST,
                    entity,
                    UserDetails.class);

            UserDetails userDetails = responseEntity.getBody();
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(userDetails.getRole()));

            if (userDetails != null && userDetails.getUsername() != null) {
                return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), jwtToken);
            }

            return new UsernamePasswordAuthenticationToken(null, null);

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
