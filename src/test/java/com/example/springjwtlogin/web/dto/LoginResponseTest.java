package com.example.springjwtlogin.web.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginResponseTest {

    LoginResponse instance;

    @BeforeEach
    void setUp() {
        instance = new LoginResponse("testToken", "testUsername");
    }

    @Test
    void shouldAssertNotNullWhenCreated() {
        assertThat(instance, not(nullValue()));
    }

    @Test
    void shouldReturnNotNullWhenGetJwt() {
        assertThat(instance.getJwt(), not(nullValue()));
    }

    @Test
    void shouldReturnNotNullWhenGetUsername() {
        assertThat(instance.getUsername(), not(nullValue()));
    }

}
