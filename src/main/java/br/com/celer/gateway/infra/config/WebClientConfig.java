package br.com.celer.gateway.infra.config;

import br.com.celer.gateway.infra.properties.AutenticacaoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient authWebClient(AutenticacaoProperties autenticacaoProperties) {
        return WebClient.builder()
                .baseUrl(autenticacaoProperties.getService().getUrl())
                .build();
    }

}