package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.httpErrorHandler.CustomErrorHandler;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.*;

@Component
public class RequestBuilder {
    private static Logger logger = Logger.getLogger(RequestBuilder.class.getName());

    public ResponseEntity<String> sendRequestResponse(String url, Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String json = JSONBuilder.buildJSON(body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        logger.log(Level.FINE,"Sending http request with body: \n"+json+"\n to: "+url);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> sendRequestResponse(String url, String token) {
        RestTemplate restTemplate = new RestTemplate();
        //custom error handler to handle http response
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        headers.set("Authorization", "Bearer " + token);
        logger.log(Level.FINE,"Sending http request with no body to: "+url);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;

    }

    public ResponseEntity<String> sendRequestResponse(String url, String token, boolean get) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response;
    }


    public ResponseEntity<String> sendRequestResponse(String url, Map<String, Object> body, String token) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String json = JSONBuilder.buildJSON(body);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

}
