package com.example.springjwtlogin.web.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestTest {
  Validator validator;
  LoginRequest instance;

  @BeforeEach
  void setUp() {
    instance = new LoginRequest("testUser", "testPassword");

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldAssertNotNullWhenCreated() {
    assertThat(instance, not(nullValue()));
  }

  @Test
  void shouldReturnNotBlankViolationWhenGetUsername() {
    LoginRequest instanceWithNullUsername = new LoginRequest(null, "password");
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(instanceWithNullUsername);
    ConstraintViolation<LoginRequest> violation = violations.stream().findFirst().get();

    assertThat(violation.getPropertyPath().toString(), equalTo("username"));
    assertThat(violation.getInvalidValue(), nullValue());
  }

  @Test
  void shouldReturnNotNullWhenGetUsername() {
    assertThat(instance.getUsername(), not(nullValue()));
  }

  @Test
  void shouldReturnNotBlankWhenGetUsername() {
    assertThat(instance.getUsername(), not(blankString()));
  }

  @Test
  void shouldReturnNotBlankViolationWhenGetPassword() {
    LoginRequest instanceWithNullUsername = new LoginRequest("username", null);
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(instanceWithNullUsername);
    ConstraintViolation<LoginRequest> violation = violations.stream().findFirst().get();

    assertThat(violation.getPropertyPath().toString(), equalTo("password"));
    assertThat(violation.getInvalidValue(), nullValue());
  }

  @Test
  void shouldReturnNotNullWhenGetPassword() {
    assertThat(instance.getPassword(), not(nullValue()));
  }

  @Test
  void shouldReturnNotBlankWhenGetPassword() {
    assertThat(instance.getPassword(), not(blankString()));
  }

}
