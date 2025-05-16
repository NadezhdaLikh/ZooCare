package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.AuthRequest;
import com.spring.zoocare.models.dtos.responses.AuthResponse;
import com.spring.zoocare.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Аутентификация")
    public AuthResponse authenticate(@RequestBody @Valid AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @GetMapping("/home")
    @Operation(summary = "Перейти на главную страницу")
    public ResponseEntity<String> showHomePage() {
        return ResponseEntity.ok("Добро пожаловать!");
    }
}

