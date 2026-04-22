package br.com.celer.gateway.infra.client;

import br.com.celer.gateway.http.dto.ValidacaoTokenRequestDTO;
import br.com.celer.gateway.http.dto.ValidacaoTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AutenticacaoClient {

    private final WebClient authWebClient;

    public Mono<ValidacaoTokenResponseDTO> validarToken(ValidacaoTokenRequestDTO request) {
        return authWebClient.post()
                .uri("/v1/autenticacao/validar")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ValidacaoTokenResponseDTO.class);
    }
}