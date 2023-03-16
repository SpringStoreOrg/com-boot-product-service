package com.boot.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackages = {"com.boot.product.model",
        "org.axonframework.modelling.saga.repository.jpa",
        "org.axonframework.eventhandling.tokenstore.jpa",
        "org.axonframework.eventsourcing.eventstore.jpa"
})
public class SpringProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProductServiceApplication.class, args);
    }
}
