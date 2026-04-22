package br.com.celer.gateway.infra.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

import static br.com.celer.gateway.http.util.HttpConstants.HEADER_CORRELATION_ID;
import static br.com.celer.gateway.infra.util.CelerConstants.CORRELATION_ID;
import static br.com.celer.gateway.infra.util.OrdemFiltros.PRIMEIRO;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final int TAMANHO_MAXIMO_CORRELATION_ID = 64;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = obterOuGerarCorrelationId(exchange.getRequest().getHeaders());

        ServerHttpRequest requestMutado = exchange.getRequest()
                .mutate()
                .headers(headers -> headers.set(HEADER_CORRELATION_ID, correlationId))
                .build();

        ServerWebExchange exchangeMutado = exchange.mutate()
                .request(requestMutado)
                .build();

        exchangeMutado.getAttributes().put(CORRELATION_ID, correlationId);

        exchangeMutado.getResponse().beforeCommit(() -> {
            exchangeMutado.getResponse().getHeaders().set(HEADER_CORRELATION_ID, correlationId);
            return Mono.empty();
        });

        return chain.filter(exchangeMutado)
                .contextWrite(Context.of(CORRELATION_ID, correlationId));
    }

    @Override
    public int getOrder() {
        return PRIMEIRO;
    }

    private String obterOuGerarCorrelationId(HttpHeaders headers) {
        String correlationId = headers.getFirst(HEADER_CORRELATION_ID);
        return isValidoParaPropagacao(correlationId)
                ? correlationId.trim()
                : UUID.randomUUID().toString();
    }

    /**
     * Validação propositalmente permissiva para preservar a propagação do correlation id
     * entre sistemas heterogêneos. A preocupação aqui é garantir presença e evitar valores
     * excessivamente grandes, sem impor formato rígido que possa quebrar a rastreabilidade.
     */
    private boolean isValidoParaPropagacao(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            return false;
        }
        return correlationId.trim().length() <= TAMANHO_MAXIMO_CORRELATION_ID;
    }
}