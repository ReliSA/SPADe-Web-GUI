package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OAuthServiceImplTest {

    @Autowired
    private OAuthService oAuthService;

    @MockBean
    private RequestBuilder requestBuilder;

    /**
     * Mocked User
     */
    private final User mockUser = new User("foo", "foo@foo.cz", "foo");
    private final String sampleToken = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyMGU4NmJiMC1lMDU4LTQwYTMtYjQyZC02ZTBjOWIyMmQ5MWQiL" +
            "CJzdWIiOiJmb28iLCJpYXQiOjE2ODE0MTY2ODAsImV4cCI6MTY4MTQxNjk4MH0.YJTwPEI4njQqYRuLGilf_oVl0gGD5BWFmdolk1O" +
            "vYWIgTcoIAFwh6bwqrW7XM6Hlj-ItYj9EmihYIqN5gfJVtg";
    // valid user logged in - http response with code 200 and token in body is expected
    @Test
    public void loginValidUser() {
        ResponseEntity<String> response = ResponseEntity.ok().body(sampleToken);

        when(requestBuilder.sendRequestResponse(anyString(), anyMap())).thenReturn(response);
        ResponseEntity<String> response1 = oAuthService.loginUser(mockUser);
        assertEquals(response, response1);

    }

    // return 200 response if valid user is trying to authenticate
    @Test
    public void authenticateValidUser() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok().body(mockUser.getName());

        when(requestBuilder.sendRequestResponse(anyString(), anyString())).thenReturn(expectedResponse);
        ResponseEntity<String> response1 = oAuthService.authenticate(sampleToken);
        assertEquals(expectedResponse, response1);

//        try(MockedStatic<RequestBuilder> requestBuilderMockedStatic = Mockito.mockStatic(RequestBuilder.class)){
//            requestBuilderMockedStatic.when(() -> RequestBuilder.sendRequestResponse(anyString(), anyString())).thenReturn(expectedResponse);
//            ResponseEntity<String> response = oAuthService.authenticate(sampleToken);
//            assertEquals(expectedResponse, response);
//        }

    }

    // return 401 if unauthorized user is trying to authenticate
    @Test
    public void authenticateInvalidUser() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mockUser.getName());

        when(requestBuilder.sendRequestResponse(anyString(), anyString())).thenReturn(expectedResponse);
        ResponseEntity<String> response1 = oAuthService.authenticate(sampleToken);
        assertEquals(expectedResponse, response1);

//        try(MockedStatic<RequestBuilder> requestBuilderMockedStatic = Mockito.mockStatic(RequestBuilder.class)){
//            requestBuilderMockedStatic.when(() -> RequestBuilder.sendRequestResponse(anyString(), anyString())).thenReturn(expectedResponse);
//            ResponseEntity<String> response = oAuthService.authenticate(sampleToken);
//            assertEquals(expectedResponse, response);
//        }

    }
    @Test
    public void logoutUser() {
        Map<String,Object> json = new HashMap<>();
        json.put("message","ok");
        //have to set token to the user - in this scenario we assume user is logged in
        mockUser.setToken(sampleToken);
        String jsonString = JSONBuilder.buildJSON(json);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body(jsonString);

        when(requestBuilder.sendRequestResponse(anyString(), anyMap())).thenReturn(expectedResponse);
        ResponseEntity<String> response1 = oAuthService.logoutUser(mockUser);
        assertEquals(expectedResponse, response1);

//        try(MockedStatic<RequestBuilder> requestBuilderMockedStatic = Mockito.mockStatic(RequestBuilder.class)){
//            requestBuilderMockedStatic.when(() -> RequestBuilder.sendRequestResponse(anyString(), anyMap())).thenReturn(expectedResponse);
//            ResponseEntity<String> response = oAuthService.logoutUser(mockUser);
//            assertEquals(expectedResponse, response);
//        }


    }










}
