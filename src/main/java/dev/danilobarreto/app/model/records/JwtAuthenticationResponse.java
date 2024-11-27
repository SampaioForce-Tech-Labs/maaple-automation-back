package dev.danilobarreto.app.model.records;

public record JwtAuthenticationResponse(
        String accessToken,
        String tokenType) {
}
