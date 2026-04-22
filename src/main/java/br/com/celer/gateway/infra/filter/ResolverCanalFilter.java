package br.com.celer.gateway.infra.filter;

import br.com.celer.gateway.domain.enums.Canal;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.celer.gateway.domain.enums.Canal.ECOMMERCE;
import static br.com.celer.gateway.http.util.HttpConstants.*;
import static br.com.celer.gateway.infra.util.CelerConstants.CANAL;
import static br.com.celer.gateway.infra.util.CelerConstants.TENANCIA;
import static br.com.celer.gateway.infra.util.OrdemFiltros.SEGUNDO;
import static java.util.Locale.ROOT;
import static org.springframework.util.StringUtils.hasText;

@Component
public class ResolverCanalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String host = extrairHost(exchange.getRequest());

        if (!hasText(host)) {
            return bloquear(exchange);
        }

        if (host.equals(DOMINIO_ECOMMERCE) || host.endsWith("." + DOMINIO_ECOMMERCE)) {
            return resolverEcommerce(exchange, chain, host);
        }

        if (host.equals(DOMINIO_CORPORATIVO) || host.endsWith("." + DOMINIO_CORPORATIVO)) {
            return resolverCorporativo(exchange, chain, host);
        }

        return bloquear(exchange);
    }

    private Mono<Void> resolverEcommerce(ServerWebExchange exchange, GatewayFilterChain chain, String host) {
        if (host.equals(DOMINIO_ECOMMERCE)) {
            return bloquear(exchange);
        }

        String tenancia = host.substring(0, host.length() - DOMINIO_ECOMMERCE.length() - 1);

        if (!hasText(tenancia) || tenancia.contains(".")) {
            return bloquear(exchange);
        }

        exchange.getAttributes().put(CANAL, ECOMMERCE);
        exchange.getAttributes().put(TENANCIA, tenancia);

        return chain.filter(exchange);
    }

    private Mono<Void> resolverCorporativo(ServerWebExchange exchange, GatewayFilterChain chain, String host) {
        if (host.equals(DOMINIO_CORPORATIVO)) {
            return bloquear(exchange);
        }

        String subdominio = host.substring(0, host.length() - DOMINIO_CORPORATIVO.length() - 1);

        Canal canal = switch (subdominio) {
            case "erp"  -> Canal.ERP;
            case "pdvl" -> Canal.PDVL;
            case "pdvc" -> Canal.PDVC;
            default     -> null;
        };

        if (canal == null) {
            return bloquear(exchange);
        }

        exchange.getAttributes().put(CANAL, canal);

        return chain.filter(exchange);
    }

    private Mono<Void> bloquear(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    private String extrairHost(ServerHttpRequest request) {
        String dominioOrigem = request.getHeaders().getFirst(DOMINIO_ORIGEM);
        if (hasText(dominioOrigem)) {
            return normalizar(primeiroValor(dominioOrigem));
        }

        return null;
    }

    private String primeiroValor(String valor) {
        int idx = valor.indexOf(',');
        return idx >= 0 ? valor.substring(0, idx).trim() : valor.trim();
    }

    private String normalizar(String host) {
        String normalizado = host.trim().toLowerCase(ROOT);

        int portaIdx = normalizado.indexOf(':');
        if (portaIdx > -1) {
            normalizado = normalizado.substring(0, portaIdx);
        }

        return normalizado;
    }

    @Override
    public int getOrder() {
        return SEGUNDO;
    }

}