package br.com.celer.gateway.http.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ValidacaoTokenResponseDTO {
    private Long prkUsuario;
    private String canal;
    private Map<String, Object> contexto;
}