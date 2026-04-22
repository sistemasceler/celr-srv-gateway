package br.com.celer.gateway.http.dto;

public record ValidacaoTokenRequestDTO(
        String token,
        String canal,
        String ip,
        String userAgent
) {
}