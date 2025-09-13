package com.main.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("product-service", r -> r
                        .path("/api/products/**","/api/category/**")
                        .uri("lb://PRODUCT-SERVICE"))
                .route("order-service", r -> r
                        .path("/api/carts/**","/api/order/**")
                        .uri("lb://ORDER-SERVICE"))
                .route("payment-service", r -> r
                        .path("/api/payment/**")
                        .uri("lb://PAYMENT-SERVICE"))
                .build();

    }
}
