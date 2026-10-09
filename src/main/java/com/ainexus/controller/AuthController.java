package com.ainexus.controller;

import com.ainexus.config.AuthInterceptor;
import com.ainexus.dto.LoginRequest;
import com.ainexus.dto.RegisterRequest;
import com.ainexus.model.User;
import com.ainexus.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        service.logout(request.getHeader("Authorization"));
        return Map.of("message", "Logged out successfully.");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthInterceptor.USER);
        if (user == null) {
            throw new IllegalArgumentException("Authentication required.");
        }
        return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(), "role", user.getRole());
    }
}
