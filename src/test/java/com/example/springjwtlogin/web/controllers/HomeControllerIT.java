package com.example.springjwtlogin.web.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.example.springjwtlogin.web.dto.LoginRequest;
import com.example.springjwtlogin.web.dto.LoginResponse;

import java.net.URL;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
class HelloControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/api");
    }

    @Test
    void shouldReturnOkWhenHomeIsCalled() throws Exception {

        ResponseEntity<String> response = this.authRestTemplate() //
                .getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Hello Docker World!"));

    }

    private TestRestTemplate authRestTemplate() {

        String loginPath = base.toString() + "/auth/login";
        LoginRequest loginRequest = new LoginRequest("test", "test");
        ResponseEntity<LoginResponse> response = template //
                .postForEntity(loginPath, loginRequest, LoginResponse.class);
        String authToken = response.getBody().getJwt();

        this.template.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + authToken);
            return execution.execute(request, body);
        }));
        return this.template;

    }
}
