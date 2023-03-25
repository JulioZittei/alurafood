package br.com.alurafood.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("payments", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.stripPrefix(3))
                        .uri("lb://payments"))
                .route("orders", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.stripPrefix(3))
                        .uri("lb://orders"))
                .build();
    }

}
