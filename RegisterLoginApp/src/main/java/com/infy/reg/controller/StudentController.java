package com.infy.reg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.infy.reg.entity.Student;
import com.infy.reg.repo.StudentRepo;

@RestController
public class StudentController {

	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/register")
	public ResponseEntity<String> registration (@RequestBody Student s){
		String encodedPwd = pwdEncoder.encode(s.getPassword());
		s.setPassword(encodedPwd);
		repo.save(s);
		return new ResponseEntity<String>("User Registered",HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginCheck (@RequestBody Student s){
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(s.getEmail(), s.getPassword());
		System.err.println(token.getPrincipal()+" : "+token.getCredentials());
		try {
			Authentication authenticate = authManager.authenticate(token);
			System.err.println(authManager.authenticate(authenticate).isAuthenticated());
			if(authenticate.isAuthenticated())return new ResponseEntity<String>("Welcom to AshokIT",HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
	}
}