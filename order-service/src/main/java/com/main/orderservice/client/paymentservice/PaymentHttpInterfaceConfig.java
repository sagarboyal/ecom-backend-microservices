package com.main.orderservice.client.paymentservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PaymentHttpInterfaceConfig {
    @Bean
    public PaymentHttpInterfaceProvider paymentHttpInterfaceProvider(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://payment-service")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(PaymentHttpInterfaceProvider.class);
    }
}
