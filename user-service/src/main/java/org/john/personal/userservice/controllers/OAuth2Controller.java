package org.john.personal.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {
    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> oauth2Success(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("expires_in", 3600);
        response.put("token_type", "Bearer");
        response.put("message", "OAuth2 login successful");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> oauth2Error(@RequestParam(required = false) String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "oauth2_login_failed");
        response.put("message", message != null ? message : "OAuth2 authentication failed");

        return ResponseEntity.badRequest().body(response);
    }
}
