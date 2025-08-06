package com.sayem.eazybank.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sayem.eazybank.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
	
	Optional<Customer> findByEmail(String email);
	
}
