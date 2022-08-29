package com.simformsolutions.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity(debug = true)
@Order(1)
public class BuyerSecurityConfiguration extends WebSecurityConfigurerAdapter {
    public BuyerSecurityConfiguration() {
        super();
    }

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/login/**", "/signup/**", "/css/**", "/lib/**", "/js/**", "/scss/**", "/img/**", "/mail/**", "/productImages/**").permitAll();

        http
                .authorizeRequests()
                .antMatchers("/buyer/**")
                .hasAuthority("buyer")
            /*    .antMatchers("/seller/**")
                .hasAuthority("seller")
*/
                .and()
                .formLogin()
                .loginPage("/login/buyer")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login/buyer")
                .failureUrl("/")

                .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new BasicAuthenticationEntryPoint())

                .and()
                .csrf().disable();

    }
}
