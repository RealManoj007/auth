package com.infy.reg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.reg.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer>{

	Student findByEmail(String email);
	
}
