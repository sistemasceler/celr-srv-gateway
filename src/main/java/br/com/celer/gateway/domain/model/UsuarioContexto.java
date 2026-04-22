package br.com.celer.gateway.domain.model;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioContexto {
    private Long usuarioPrk;
    private Long unidadePrk;
    private String sessaoId;
    private String canal;
    private List<String> roles;
}