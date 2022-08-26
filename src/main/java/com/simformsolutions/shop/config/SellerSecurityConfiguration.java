package com.simformsolutions.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity()
@Order(2)
public class SellerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/seller/**")
                .hasAnyAuthority("seller", "admin")

                .and()
                .formLogin()
                .loginPage("/login/seller")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/principal/seller", true)
                .failureUrl("/");
    }
}

