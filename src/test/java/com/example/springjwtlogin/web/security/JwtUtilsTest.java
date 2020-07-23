package com.example.springjwtlogin.web.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.springjwtlogin.security.JwtProperties;
import com.example.springjwtlogin.security.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    final String mockJwtToken = "jwtToken";
    JwtUtils jwtUtils;

    @Mock
    JwtUtils mockJwtUtils;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        props.setSecret("secret");
        props.setExpiration(60000);

        jwtUtils = new JwtUtils(props);
    }

    @Test
    void shouldGenerateTokenWhenCalled() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("test");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        String jwtToken = jwtUtils.generateJwtToken(authentication);
        assertThat(jwtToken, notNullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetUserNameWhenValidToken() {
        String mockUsername = "username";

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(mockUsername);

        Jws<Claims> mockJwsClaims = mock(Jws.class);
        when(mockJwsClaims.getBody()).thenReturn(claims);

        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenReturn(mockJwsClaims);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.getUserNameFromJwtToken(mockJwtToken)).thenCallRealMethod();

        String username = mockJwtUtils.getUserNameFromJwtToken(mockJwtToken);
        assertThat(username, notNullValue());
        assertThat(username, is(mockUsername));
    }

    @Test
    void shouldValidateTokenWhenIsValid() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenReturn(null);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(true));
    }

    @Test
    void shouldReturnFalseWhenSignatureExceceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(SignatureException.class);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenMalformedJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(MalformedJwtException.class);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenExpiredJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(ExpiredJwtException.class);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenUnsupportedJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(UnsupportedJwtException.class);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenIllegalArgumentExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(IllegalArgumentException.class);

        when(mockJwtUtils.getParser()).thenReturn(mockParser);
        when(mockJwtUtils.validateJwtToken(mockJwtToken)).thenCallRealMethod();

        boolean validation = mockJwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnParserWhenCalled(){
        JwtParser parser = jwtUtils.getParser();
        assertThat(parser, notNullValue());
    }

}
