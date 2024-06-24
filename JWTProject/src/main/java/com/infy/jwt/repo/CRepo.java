package com.infy.jwt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.jwt.entity.Customer;

public interface CRepo extends JpaRepository<Customer, Integer> {
	public Customer findByUname(String uname);
}
