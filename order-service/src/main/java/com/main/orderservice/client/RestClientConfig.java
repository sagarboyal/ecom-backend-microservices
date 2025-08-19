package com.main.orderservice.client;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class RestClientConfig {
    private final Optional<ObservationRegistry> observationRegistry;
    private final Optional<Tracer> tracer;
    private final Optional<Propagator> propagator;

    public RestClientConfig(Optional<ObservationRegistry> observationRegistry,
                            Optional<Tracer> tracer,
                            Optional<Propagator> propagator) {
        this.observationRegistry = observationRegistry;
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder restTemplate() {
        RestClient.Builder  clientBuilder = RestClient.builder();
        if (observationRegistry.isPresent()) clientBuilder.requestInterceptor(tracingInterceptor());
        return clientBuilder;
    }

    private ClientHttpRequestInterceptor tracingInterceptor() {
        return ((request, body, execution) -> {
            if(tracer.isPresent() && propagator.isPresent() && tracer.get().currentSpan() != null) {
                propagator.get().inject(Objects.requireNonNull(tracer.get().currentTraceContext().context()),
                        request.getHeaders(),
                        (carrier, key, value) -> {
                            assert carrier != null;
                            carrier.add(key, value);
                        });
            }
            return execution.execute(request, body);
        });
    }
}
