package org.john.personal.userservice.advices;

import org.john.personal.userservice.exceptions.CustomAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomAuthenticationException(CustomAuthenticationException e) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }

    @ExceptionHandler(RuntimeException.class)  // Less specific
    public ResponseEntity<?> handleRuntime(RuntimeException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        System.out.println(e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("error", "insufficient_privileges");
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }
}
