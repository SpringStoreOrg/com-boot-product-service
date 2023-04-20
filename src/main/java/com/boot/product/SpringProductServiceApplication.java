package com.boot.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"com.boot.product.model"})
public class SpringProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProductServiceApplication.class, args);
    }
}
