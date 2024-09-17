package cz.zcu.fav.kiv.antipatterndetectionapp.v2.httpExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<String> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getResponseBodyAsString());
    }

}
