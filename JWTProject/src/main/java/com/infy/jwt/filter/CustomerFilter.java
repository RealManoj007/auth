package com.infy.jwt.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.infy.jwt.service.CustomerService;
import com.infy.jwt.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerFilter  extends OncePerRequestFilter{

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CustomerService customerService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = null;
		String username = null;
	//check authorization header presence
		String header=request.getHeader("Authorization");
	
	//if header present, retrive bearer token	
	if(header != null && header.startsWith("Bearer")) {
		log.info("Header is {}",header);
		token = header.substring(7);
		log.info("Token is {}",token);
		username = jwtService.extractUsername(token);
		log.info("Username is  is {}",username);	
	}
	
	//validate token
	if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
		UserDetails userDetails = customerService.loadUserByUsername(username);
		if(jwtService.validateToken(token, userDetails)) {
			UsernamePasswordAuthenticationToken authToken = 
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
			
		}
	}
	//if token is valid, update security context
	filterChain.doFilter(request, response);
		
	}
}
