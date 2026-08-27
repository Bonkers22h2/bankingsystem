package com.banking.bankingsystem.controller;

import com.banking.bankingsystem.dto.LoginRequest;
import com.banking.bankingsystem.dto.RegisterRequest;
import com.banking.bankingsystem.model.User;
import com.banking.bankingsystem.service.AuthService;
import com.banking.bankingsystem.service.JwtService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE
})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        String token = jwtService.generateToken(user.getUsername(), user.getRole().toString());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}