package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class MainController {

    @GetMapping(value = "/testCall", produces = "application/json")
    public ResponseEntity<String> TestController() {
        String response = "{\"response\": \"successfully called\"}";

        return ResponseEntity.ok().body(response);
    }

}
