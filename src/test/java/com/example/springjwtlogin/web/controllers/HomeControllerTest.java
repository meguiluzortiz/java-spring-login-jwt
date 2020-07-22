package com.example.springjwtlogin.web.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.springjwtlogin.security.AuthenticationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(HomeController.class)
@Import({ AuthenticationService.class })
// @SpringBootTest
// @AutoConfigureMockMvc
class HelloControllerTest {
  final String TEST_USERNAME = "test";
  final String HOME_PATH = "/";

  // MockMvc comes from Spring Test and lets you, through a set of convenient
  // builder classes, send HTTP requests into the DispatcherServlet and make
  // assertions about the result.
  @Autowired
  private MockMvc mvc;

  @WithMockUser(value = TEST_USERNAME)
  @Test
  void shouldReturnOkWhenHomeIsCalled() throws Exception {
    // Given user is equals to test
    // When home is called
    MvcResult result = mvc.perform(get(HOME_PATH).accept(APPLICATION_JSON)).andReturn();

    // Expect Ok
    MockHttpServletResponse response = result.getResponse();
    assertThat(response.getStatus(), equalTo(200));
  }

  @WithMockUser(value = TEST_USERNAME)
  @Test
  void shouldReturnDefaultMessageWhenHomeIsCalled() throws Exception {
    // Given user is equals to test
    // When home is called
    MvcResult result = mvc.perform(get(HOME_PATH).accept(APPLICATION_JSON)).andReturn();

    // Expect Ok
    MockHttpServletResponse response = result.getResponse();
    assertThat(response.getContentAsString(), equalTo("Hello Docker World!"));
  }

  @Test
  void shouldReturnUnauthorizedWhenHomeIsCalledWithNoCredentials() throws Exception {
    // Given user is equals to test
    // When home is called
    MvcResult result = mvc.perform(get(HOME_PATH).accept(APPLICATION_JSON)).andReturn();

    // Expect Ok
    MockHttpServletResponse response = result.getResponse();
    assertThat(response.getStatus(), equalTo(401));
  }

}
