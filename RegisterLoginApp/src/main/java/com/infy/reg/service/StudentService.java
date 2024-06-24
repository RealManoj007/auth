package com.infy.reg.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infy.reg.entity.Student;
import com.infy.reg.repo.StudentRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentService implements UserDetailsService{
	
	@Autowired
	private StudentRepo studentRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("username is {}",username);
		Student student = studentRepo.findByEmail(username);
		
		User usr=new User(student.getEmail(), student.getPassword(), Collections.EMPTY_LIST);
		
		return usr;
	}

}
