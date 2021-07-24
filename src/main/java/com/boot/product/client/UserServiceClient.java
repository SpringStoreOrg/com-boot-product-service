package com.boot.product.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.boot.product.util.Constants;
import com.boot.services.dto.UserDTO;

public class UserServiceClient {
	
	@Autowired
	private RestTemplate restTemplate;

	public List<UserDTO> callGetAllUsers() {

		UserDTO[] userArray = restTemplate.getForEntity(Constants.GET_ALL_USERS, UserDTO[].class).getBody();

		return Arrays.asList(userArray);
	}

	public void callUpdateUser(String userName, UserDTO user) {
		restTemplate.exchange(Constants.UPDATE_USER + userName, HttpMethod.PUT, new HttpEntity<>(user), String.class);
	}
}
