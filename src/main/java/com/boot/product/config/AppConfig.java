package com.boot.product.config;

import com.boot.product.service.ProductCommandHandler;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Bean(name = "userServiceRestTemplate")
    public RestTemplate userServiceRestTemplateUrl() {
        return new RestTemplateBuilder().rootUri(userServiceUrl).build();
    }

    @Bean
    public CommandDispatcher consumerCommandDispatcher(ProductCommandHandler target,
                                                       SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("customerCommandDispatcher", target.commandHandlerDefinitions());
    }
}
