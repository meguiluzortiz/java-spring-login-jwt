package com.example.springjwtlogin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.jwt")
public class JwtProperties {
  private String secret;
  private Integer expiration;

}
