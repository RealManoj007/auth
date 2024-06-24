package com.infy.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

	@GetMapping("/")
	public String welcome() {
		return "Welcome to AshokIt";
	}
	
}
