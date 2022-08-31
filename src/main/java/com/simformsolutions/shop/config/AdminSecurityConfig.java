package com.simformsolutions.shop.config;

import com.simformsolutions.shop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AdminSecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll();

		http.antMatcher("/seller/**")
			.authorizeRequests().anyRequest().hasAuthority("seller")
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.loginProcessingUrl("/admin/login")
				.defaultSuccessUrl("/admin/home")
				.failureUrl("/")
				.permitAll()
			.and()
			.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/");

		return http.build();
	}
    
}
