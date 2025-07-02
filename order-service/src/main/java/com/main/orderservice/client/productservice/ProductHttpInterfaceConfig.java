package com.main.orderservice.client.productservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ProductHttpInterfaceConfig {
    @Bean
    public ProductHttpInterfaceProvider productHttpInterfaceProvider(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://product-service")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(ProductHttpInterfaceProvider.class);
    }
}
