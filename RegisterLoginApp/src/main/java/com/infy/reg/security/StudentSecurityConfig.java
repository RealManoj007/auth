package com.infy.reg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.infy.reg.service.StudentService;

@Configuration
@EnableWebSecurity
public class StudentSecurityConfig {

	@Autowired
	private StudentService service;
	
	@Bean
	public PasswordEncoder getPwdEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//set AuthenticationProvider
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(getPwdEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		System.err.println("Auth carry data is "+auth.getAuthenticationManager());
		return auth.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception{
		return http.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/register","/login")
				.permitAll()
				.and()
				.build();
	}
}
