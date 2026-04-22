package br.com.celer.gateway.infra.util;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

public final class OrdemFiltros {

    public static final int PRIMEIRO  = HIGHEST_PRECEDENCE + 100;
    public static final int SEGUNDO   = HIGHEST_PRECEDENCE + 200;
    public static final int TERCEIRO  = HIGHEST_PRECEDENCE + 300;
    public static final int QUARTO    = HIGHEST_PRECEDENCE + 400;
    public static final int QUINTO    = HIGHEST_PRECEDENCE + 500;
    public static final int SEXTO     = HIGHEST_PRECEDENCE + 600;

    private OrdemFiltros() {}
}