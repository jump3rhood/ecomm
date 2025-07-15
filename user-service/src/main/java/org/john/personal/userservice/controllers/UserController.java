package org.john.personal.userservice.controllers;

import org.john.personal.userservice.dtos.request.LoginRequestDto;
import org.john.personal.userservice.dtos.request.RegisterRequestDto;
import org.john.personal.userservice.dtos.response.UserResponseDTO;
import org.john.personal.userservice.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterRequestDto requestDto){
        return ResponseEntity.ok(service.registerUser(requestDto));
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity.ok(service.login(requestDto.getUsername(), requestDto.getPassword()));
    }
}
