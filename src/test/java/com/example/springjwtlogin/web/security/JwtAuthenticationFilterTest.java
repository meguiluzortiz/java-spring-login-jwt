package com.example.springjwtlogin.web.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import com.example.springjwtlogin.security.JwtAuthenticationFilter;
import com.example.springjwtlogin.security.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    String mockJwtToken = "jwtToken";
    String mockUsername = "username";
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Mock
    MockFilterChain chain;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void shouldContinueWhenEmptyAuthorizationHeader() throws ServletException, IOException {
        request.addHeader("Authorization", "");
        jwtAuthenticationFilter.doFilter(request, response, chain);
        verify(jwtUtils, times(0)).validateJwtToken(mockJwtToken);
        verify(jwtUtils, times(0)).getUserNameFromJwtToken(mockJwtToken);
        verify(userDetailsService, times(0)).loadUserByUsername(mockUsername);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldContinueWhenNoValidAuthorizationHeader() throws ServletException, IOException {
        request.addHeader("Authorization", "NotBearer " + mockJwtToken);
        jwtAuthenticationFilter.doFilter(request, response, chain);
        verify(jwtUtils, times(0)).validateJwtToken(mockJwtToken);
        verify(jwtUtils, times(0)).getUserNameFromJwtToken(mockJwtToken);
        verify(userDetailsService, times(0)).loadUserByUsername(mockUsername);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldContinueWhenNoValidJwtToken() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer " + mockJwtToken);
        when(jwtUtils.validateJwtToken(mockJwtToken)).thenReturn(false);
        jwtAuthenticationFilter.doFilter(request, response, chain);
        verify(jwtUtils, times(1)).validateJwtToken(mockJwtToken);
        verify(jwtUtils, times(0)).getUserNameFromJwtToken(mockJwtToken);
        verify(userDetailsService, times(0)).loadUserByUsername(mockUsername);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldThrowExceptionWhenUsernameNotFound() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer " + mockJwtToken);
        when(jwtUtils.validateJwtToken(mockJwtToken)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(mockJwtToken)).thenReturn(mockUsername);
        when(userDetailsService.loadUserByUsername(mockUsername)).thenThrow(UsernameNotFoundException.class);
        jwtAuthenticationFilter.doFilter(request, response, chain);
        verify(jwtUtils, times(1)).validateJwtToken(mockJwtToken);
        verify(jwtUtils, times(1)).getUserNameFromJwtToken(mockJwtToken);
        verify(userDetailsService, times(1)).loadUserByUsername(mockUsername);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldAutenticateWhenUsernameFound() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer " + mockJwtToken);
        when(jwtUtils.validateJwtToken(mockJwtToken)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(mockJwtToken)).thenReturn(mockUsername);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(mockUsername)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilter(request, response, chain);
        verify(jwtUtils, times(1)).validateJwtToken(mockJwtToken);
        verify(jwtUtils, times(1)).getUserNameFromJwtToken(mockJwtToken);
        verify(userDetailsService, times(1)).loadUserByUsername(mockUsername);
        verify(chain, times(1)).doFilter(request, response);
    }

}
