package com.boot.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.boot.product.client.UserServiceClient;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

	@Bean
	public UserServiceClient userServiceClient() {
		return new UserServiceClient();
	}

}
