package org.example.abysaltobackendtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.abysaltobackendtask.dto.auth.LoginRequest;
import org.example.abysaltobackendtask.dto.auth.LoginResponse;
import org.example.abysaltobackendtask.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req){
        return authService.login(req);
    }
}
