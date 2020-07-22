package com.example.springjwtlogin.web.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.springjwtlogin.security.JwtProperties;
import com.example.springjwtlogin.security.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import mockit.Mock;
import mockit.MockUp;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    final String mockJwtToken = "jwtToken";
    JwtUtils jwtUtils;

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

        this.mockJwtsParser(mockParser);

        String username = jwtUtils.getUserNameFromJwtToken(mockJwtToken);
        assertThat(username, notNullValue());
        assertThat(username, is(mockUsername));
    }

    @Test
    void shouldValidateTokenWhenIsValid() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenReturn(null);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(true));
    }

    @Test
    void shouldReturnFalseWhenSignatureExceceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(SignatureException.class);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenMalformedJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(MalformedJwtException.class);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenExpiredJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(ExpiredJwtException.class);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenUnsupportedJwtExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(UnsupportedJwtException.class);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    @Test
    void shouldReturnFalseWhenIllegalArgumentExceptionIsThrown() {
        JwtParser mockParser = mock(JwtParser.class);
        when(mockParser.parseClaimsJws(mockJwtToken)).thenThrow(IllegalArgumentException.class);

        this.mockJwtsParser(mockParser);

        boolean validation = jwtUtils.validateJwtToken(mockJwtToken);
        assertThat(validation, notNullValue());
        assertThat(validation, equalTo(false));
    }

    private void mockJwtsParser(JwtParser jwtParser) {
        new MockUp<Jwts>() {
            @Mock
            public JwtParser parser() {
                return jwtParser;
            }
        };
    }

}
