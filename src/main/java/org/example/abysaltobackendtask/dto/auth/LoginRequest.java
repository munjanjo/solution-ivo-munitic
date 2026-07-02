package org.example.abysaltobackendtask.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}
