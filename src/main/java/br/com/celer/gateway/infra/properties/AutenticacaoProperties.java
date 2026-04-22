package br.com.celer.gateway.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "autenticacao")
public class AutenticacaoProperties {

    private Service service = new Service();

    @Data
    public static class Service {
        private String url;
    }
}