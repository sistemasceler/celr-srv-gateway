// br/com/celer/gateway/model/UsuarioContexto.java
package br.com.celer.gateway.model;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioContexto {
    private String usuarioId;
    private String unidadeId;
    private String sessaoId;
    private String canal;
    private List<String> roles;
}