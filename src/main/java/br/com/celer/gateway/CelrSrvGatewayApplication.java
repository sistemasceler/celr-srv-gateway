package br.com.celer.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CelrSrvGatewayApplication {

	public static void main(String[] args) {


		run(CelrSrvGatewayApplication.class, args);
	}
}