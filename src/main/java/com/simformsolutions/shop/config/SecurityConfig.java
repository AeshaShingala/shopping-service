/*
package com.simformsolutions.shop.config;

import com.simformsolutions.shop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class BuyerSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Bean
        public UserDetailsService userDetailsService() {
            return new CustomUserDetailsService();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService());
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/buyer/**")
                    .hasAuthority("buyer")

                    .and()
                    .formLogin()
                    .loginPage("/login/buyer")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login/buyer/principal")
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            exception.printStackTrace();
                        }
                    })
                    .and()

                    .logout()
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()

                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class SellerSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/seller/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasAuthority("seller")
                    .antMatchers("/", "/login/**", "/signup/**", "/css/**", "/lib/**", "/js/**", "/scss/**", "/img/**", "/mail/**", "/productImages/**")
                    .permitAll()

                    .and()
                    .formLogin()
                    .loginPage("/login/seller")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login/seller/principal")
                    .failureUrl("/")
                    .permitAll();
        }
    }
}*/
