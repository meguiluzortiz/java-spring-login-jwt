package com.example.springjwtlogin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.example.springjwtlogin.web.controllers.HomeController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringJwtLoginApplicationTests {
  @Autowired
  private ApplicationContext context;

  @Test
  void shouldLoadContextWhenApplicationHasStarted() {

    // WHEN
    SpringJwtLoginApplication.main(new String[] {});

    // EXPECT
    HomeController bean = context.getBean(HomeController.class);
    assertThat(bean, not(nullValue()));
  }

}
