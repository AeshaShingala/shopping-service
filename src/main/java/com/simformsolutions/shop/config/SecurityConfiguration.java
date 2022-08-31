package com.simformsolutions.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.simformsolutions.auction.filter.JwtFilter;
import com.simformsolutions.auction.handler.CustomAccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

//	@Autowired
//	private AuthenticationManager authenticationManager;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();                                  
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
	    return new CustomAccessDeniedHandler();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().formLogin().disable()
		//Authorization Mapping 
		.authorizeHttpRequests()
		.antMatchers("/admins/**").hasAuthority("SA_ADMIN")
		.antMatchers("/admin/register","/auctioneers").hasAnyAuthority("ADMIN","SA_ADMIN")
		.antMatchers("/auctioneer/register","/auctionhouse/register", "/auction/register", "/lots/**").hasAnyAuthority("AUCTIONEER","ADMIN","SA_ADMIN")
				.antMatchers("/","/auctionHouseImage/**","/auctionImage/**","/lotsImage/**","/admin/login/**","/bidder/login/**","/auctioneer/login/**", "/auctions", "/authenticate","/access", "/css/**", "/js/**").permitAll()
				.and().exceptionHandling().accessDeniedPage("/access")
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		
		//Filter For Jwt
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		//Logout Api
		http.logout(
				logout -> logout                                                
	            .logoutUrl("/logout")                                            
	            .logoutSuccessUrl("/")                                                              
	            .invalidateHttpSession(true)                                                                          
	            .deleteCookies("token"));
	}
}
