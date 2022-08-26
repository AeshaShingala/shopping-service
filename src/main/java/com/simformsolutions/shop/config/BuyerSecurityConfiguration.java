package com.simformsolutions.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity()
@Order(1)
public class BuyerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/buyer/**").hasAuthority("buyer")
                .antMatchers("/", "/login/**", "/principal/**", "/signup/**", "/css/**", "/lib/**", "/js/**", "/scss/*","/img/**","/mail/**","/productImages").permitAll()

                .and()
                .formLogin()
                .loginPage("/login/buyer")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/principal/buyer")
                .failureUrl("/")

                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()

                .and()
                .csrf().disable();
    }
}
