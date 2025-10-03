package com.github.com.Nayan_Mudewar.Online.Quiz.App.controller;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.AuthResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.LoginRequest;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.RegisterRequest;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}