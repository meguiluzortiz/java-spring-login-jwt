package com.example.springjwtlogin.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springjwtlogin.security.AuthenticationService;
import com.example.springjwtlogin.web.dto.LoginRequest;
import com.example.springjwtlogin.web.dto.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    final String AUTH_LOGIN_PATH = "/auth/login";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AuthenticationService authenticationService;

    @Test
    void shouldReturnOkWhenValidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse loginResponse = new LoginResponse("jwt", "username");
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mvc.perform(post(AUTH_LOGIN_PATH).content(toJson(loginRequest)).contentType(APPLICATION_JSON)) //
                .andDo(MockMvcResultHandlers.print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(jsonPath("$.jwt").value(loginResponse.getJwt())) //
                .andExpect(jsonPath("$.username").value(loginResponse.getUsername()));
    }

    @Test
    void shouldReturnBadRequestWhenNullUsername() throws Exception {
        LoginRequest loginRequest = new LoginRequest(null, "password");

        mvc.perform(post(AUTH_LOGIN_PATH).content(toJson(loginRequest)).contentType(APPLICATION_JSON)) //
                .andDo(MockMvcResultHandlers.print()) //
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmptyUsername() throws Exception {
        LoginRequest loginRequest = new LoginRequest("", "password");

        mvc.perform(post(AUTH_LOGIN_PATH).content(toJson(loginRequest)).contentType(APPLICATION_JSON)) //
                .andDo(MockMvcResultHandlers.print()) //
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenNullPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", null);

        mvc.perform(post(AUTH_LOGIN_PATH).content(toJson(loginRequest)).contentType(APPLICATION_JSON)) //
                .andDo(MockMvcResultHandlers.print()) //
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmptyPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "");

        mvc.perform(post(AUTH_LOGIN_PATH).content(toJson(loginRequest)).contentType(APPLICATION_JSON)) //
                .andDo(MockMvcResultHandlers.print()) //
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
