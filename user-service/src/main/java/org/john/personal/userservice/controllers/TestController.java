package org.john.personal.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        System.out.println("check");
        return ResponseEntity.ok("OK");
    }

}
