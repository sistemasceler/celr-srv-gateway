package br.com.celer.gateway.infra.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@ConfigurationProperties(prefix = "celer.gateway")
public record GatewayProperties(
        Seguranca seguranca,
        Autenticacao autenticacao,
        Canais canais,
        HeadersInternos headersInternos,
        Fallback fallback,
        Auditoria auditoria,
        RateLimit rateLimit
) {

    public record Seguranca(
            boolean removerHeadersClienteSensiveis,
            boolean bloquearHeaderContextoForjado,
            boolean validarOrigemCanal,
            boolean propagarCorrelationId
    ) {}

    public record Autenticacao(
            String url,
            Duration timeout,
            boolean failClosed,
            CacheLocal cacheLocal
    ) {
        public record CacheLocal(
                boolean habilitado,
                Duration ttl,
                int capacidadeMaxima
        ) {}
    }

    public record Canais(
            Map<String, String> hosts
    ) {}

    public record HeadersInternos(
            String usuarioId,
            String unidadeId,
            String canal,
            String permissoes,
            String sessaoId,
            String correlationId
    ) {}

    public record Fallback(
            boolean incluirDetalhes,
            String mensagemPadrao
    ) {}

    public record Auditoria(
            boolean habilitado
    ) {}

    public record RateLimit(
            Map<String, PerfilRateLimit> perfis
    ) {}

    public record PerfilRateLimit(
            int replenishRate,
            int burstCapacity
    ) {}
}