package com.example.springjwtlogin.config;

import com.example.springjwtlogin.security.JwtAuthenticationEntryPoint;
import com.example.springjwtlogin.security.JwtAuthenticationFilter;
import com.example.springjwtlogin.security.JwtProperties;
import com.example.springjwtlogin.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() //
                .withUser("test") //
                .password("{noop}test") // Spring Security 5 requires specifying the password storage format)
                .roles("TEST");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and() //
                .csrf().disable() // No csrf protection because tokens are immune to it.
                .exceptionHandling().authenticationEntryPoint(customAuthEntryPoint()).and() //
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.antMatcher("/**") //
                .authorizeRequests() //
                .antMatchers(HttpMethod.POST, "/auth/login").anonymous()//
                .antMatchers("/").authenticated() //
                .anyRequest().denyAll();

        http.addFilterBefore( //
                new JwtAuthenticationFilter(userDetailsServiceBean(), jwtUtils(jwtProperties)), //
                UsernamePasswordAuthenticationFilter.class //
        );
    }

    @Bean("userDetailsService")
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationEntryPoint customAuthEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtUtils jwtUtils(JwtProperties jwtProperties) {
        return new JwtUtils(jwtProperties);
    }

}
