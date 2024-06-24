package com.infy.jwt.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infy.jwt.entity.Customer;
import com.infy.jwt.repo.CRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService implements UserDetailsService{

	@Autowired
	private CRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Customer customer = repo.findByUname(username);
		log.info("Customer is {}",customer);
		return new User(customer.getUname(), customer.getPwd(), Collections.EMPTY_LIST);
	}

}
