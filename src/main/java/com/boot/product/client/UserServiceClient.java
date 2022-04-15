package com.boot.product.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.boot.product.util.Constants;
import com.boot.services.dto.UserDTO;

//TODO if you annotate this with @Component, you will let spring create an instance of it for you
public class UserServiceClient {

    @Autowired
    private RestTemplate userServiceRestTemplate;

    public List<UserDTO> callGetAllUsers() {

        UserDTO[] userArray = userServiceRestTemplate.getForEntity(Constants.GET_ALL_USERS, UserDTO[].class).getBody();

        return Arrays.asList(userArray);
    }

    public void callUpdateUser(String email, UserDTO user) {
        userServiceRestTemplate.exchange(Constants.UPDATE_USER + email, HttpMethod.PUT, new HttpEntity<>(user), String.class);
    }
}
