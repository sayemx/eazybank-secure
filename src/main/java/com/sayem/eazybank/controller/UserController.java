package com.sayem.eazybank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.CustomerRepository;

@RestController
public class UserController {
	
	private CustomerRepository customerRepository;
	
	private PasswordEncoder passwordEncoder;
	
	public UserController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	
	@PostMapping("/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody Customer customer){
		
		try {
			String hashPassword = passwordEncoder.encode(customer.getPwd());
			customer.setPwd(hashPassword);
			customerRepository.save(customer);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("User is created with ID: " + customer.getId());
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An exception occured: " + ex.getMessage());
		}
	}
	
}
