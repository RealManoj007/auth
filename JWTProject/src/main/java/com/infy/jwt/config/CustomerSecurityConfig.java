package com.infy.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.infy.jwt.filter.CustomerFilter;
import com.infy.jwt.service.CustomerService;

@Configuration
public class CustomerSecurityConfig {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerFilter customerFilter;
	
	@Bean
	public PasswordEncoder getEncoder() {return new BCryptPasswordEncoder();}
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(customerService);
		provider.setPasswordEncoder(getEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration conf) throws Exception {
		return conf.getAuthenticationManager();
	}
	
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
		
		return http.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/register","/login")
				.permitAll()
				.and()
				.authorizeRequests()
				.requestMatchers("/api/**")
				.authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//No Session
				.and()
				.authenticationProvider(authProvider())
				.addFilterBefore(customerFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
				
				
		
	}
	
}
