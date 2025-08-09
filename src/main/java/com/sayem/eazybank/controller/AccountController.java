package com.sayem.eazybank.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.Accounts;
import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.AccountsRepository;
import com.sayem.eazybank.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myAccountOld")
    public Accounts getAccountDetailsById(@RequestParam long id) {
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }
    
    @GetMapping("/myAccount")
    public Accounts getAccountDetailsByEmail(@RequestParam String email) {
    	Optional<Customer> customer = customerRepository.findByEmail(email);
    	if(customer.isEmpty()) return null;
    	
        Accounts accounts = accountsRepository.findByCustomerId(customer.get().getId());
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }

}
