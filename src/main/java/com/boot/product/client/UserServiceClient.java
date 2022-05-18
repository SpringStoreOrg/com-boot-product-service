package com.boot.product.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.boot.product.util.Constants;
import com.boot.services.dto.UserDTO;

@Component
public class UserServiceClient {

    @Autowired
    private RestTemplate userServiceRestTemplate;

    public List<UserDTO> callGetAllUsers() {

        UserDTO[] userArray = userServiceRestTemplate.getForEntity(Constants.GET_ALL_USERS, UserDTO[].class).getBody();

        return Arrays.asList(userArray);
    }

    public void callUpdateUser(String email, UserDTO user) {
        userServiceRestTemplate.exchange(Constants.UPDATE_USER, HttpMethod.PUT, new HttpEntity<>(user), String.class, email);
    }
}
