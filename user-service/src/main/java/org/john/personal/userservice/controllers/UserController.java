package org.john.personal.userservice.controllers;

import org.john.personal.userservice.config.JwtUserPrincipal;
import org.john.personal.userservice.dtos.response.UserResponseDTO;
import org.john.personal.userservice.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableMethodSecurity
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // GET /profile
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof JwtUserPrincipal userPrincipal){

            Map<String, Object> profile = new HashMap<String, Object>();
            profile.put("userId", userPrincipal.getUserId());
            profile.put("email", userPrincipal.getUsername());
            profile.put("firstName", userPrincipal.getFirstName());
            profile.put("lastName", userPrincipal.getLastName());
            profile.put("roles", userPrincipal.getAuthorities());

            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.status(401).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getUserList() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/debug-roles")
    public ResponseEntity<Map<String, Object>> debugRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> debug = new HashMap<>();
        debug.put("authorities", auth.getAuthorities());
        debug.put("principal", auth.getPrincipal());
        return ResponseEntity.ok(debug);
    }
}
