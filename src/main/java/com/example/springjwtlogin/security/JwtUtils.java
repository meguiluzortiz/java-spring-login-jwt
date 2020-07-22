package com.example.springjwtlogin.security;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

  private String jwtSecret;
  private int jwtExpirationMs;

  public JwtUtils(JwtProperties props) {
    this.jwtSecret = props.getSecret();
    this.jwtExpirationMs = props.getExpiration();
  }

  public String generateJwtToken(Authentication authentication) {

    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
    String username = userPrincipal.getUsername();

    return Jwts.builder() //
        .setSubject(username) //
        .setIssuedAt(new Date()) //
        .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) //
        .signWith(SignatureAlgorithm.HS512, jwtSecret) //
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    JwtParser parser = Jwts.parser();
    parser.setSigningKey(jwtSecret);
    Jws<Claims> jwsClaims = parser.parseClaimsJws(token);
    Claims claims = jwsClaims.getBody();
    return claims.getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      JwtParser parser = Jwts.parser();
      parser.setSigningKey(jwtSecret);
      parser.parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
