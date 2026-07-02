package org.example.abysaltobackendtask.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.abysaltobackendtask.dto.auth.LoginRequest;
import org.example.abysaltobackendtask.dto.auth.LoginResponse;
import org.example.abysaltobackendtask.dto.dummyjson.DummyJsonAuthResponse;
import org.example.abysaltobackendtask.exception.InvalidCredentialsException;
import org.example.abysaltobackendtask.security.JwtService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request){
        try {
            DummyJsonAuthResponse dummyResponse=restClient.post()
                    .uri("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(DummyJsonAuthResponse.class);
            String token = jwtService.generateToken(dummyResponse.username());
            log.info("User {} logged in successfully", dummyResponse.username());
            return new LoginResponse(token);
        }catch (RestClientResponseException e){
            log.warn("Failed login attempt for user '{}'", request.username());
            throw new InvalidCredentialsException();
        }


    }
}
