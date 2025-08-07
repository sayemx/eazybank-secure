package com.sayem.eazybank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sayem.eazybank.entity.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}
