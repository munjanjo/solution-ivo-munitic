package org.example.abysaltobackendtask.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "0123456789012345678901234567890123456789012345678901234567890123";

    private final JwtService jwtService = new JwtService(SECRET, 3600000L);

    @Test
    void generatedTokenIsValid() {
        String token = jwtService.generateToken("emilys");

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void extractUsernameReturnsSubject() {
        String token = jwtService.generateToken("emilys");

        assertThat(jwtService.extractUsername(token)).isEqualTo("emilys");
    }

    @Test
    void garbageTokenIsNotValid() {
        assertThat(jwtService.isTokenValid("ovo.nije.token")).isFalse();
    }

    @Test
    void tamperedTokenIsNotValid() {
        String token = jwtService.generateToken("emilys");
        String tampered = token.substring(0, token.length() - 3) + "xxx";

        assertThat(jwtService.isTokenValid(tampered)).isFalse();
    }

    @Test
    void expiredTokenIsNotValid() {
        JwtService shortLived = new JwtService(SECRET, -1000L);
        String token = shortLived.generateToken("emilys");

        assertThat(shortLived.isTokenValid(token)).isFalse();
    }
}