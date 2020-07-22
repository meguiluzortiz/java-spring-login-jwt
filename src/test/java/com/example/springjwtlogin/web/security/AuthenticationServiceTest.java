package com.example.springjwtlogin.web.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.springjwtlogin.security.AuthenticationService;
import com.example.springjwtlogin.security.JwtUtils;
import com.example.springjwtlogin.web.dto.LoginRequest;
import com.example.springjwtlogin.web.dto.LoginResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Test
    void shouldLoginWhenValidCredentials() {

        String mockJwt = "jwt";
        String mockUsername = "username";
        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        when(jwtUtils.generateJwtToken(any())).thenReturn(mockJwt);

        LoginRequest loginRequest = new LoginRequest(mockUsername, "password");
        LoginResponse loginResponse = authenticationService.login(loginRequest);

        assertThat(loginResponse.getUsername(), equalTo(mockUsername));
        assertThat(loginResponse.getJwt(), equalTo(mockJwt));
    }

}
