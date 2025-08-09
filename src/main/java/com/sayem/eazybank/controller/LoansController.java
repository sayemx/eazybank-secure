package com.sayem.eazybank.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.entity.Loans;
import com.sayem.eazybank.repository.CustomerRepository;
import com.sayem.eazybank.repository.LoanRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myLoansOld")
    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetailsById(@RequestParam long id) {
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
        if (loans != null) {
            return loans;
        } else {
            return null;
        }
    }
    
    @GetMapping("/myLoans")
    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetailsByEmail(@RequestParam String email) {
    	Optional<Customer> customer = customerRepository.findByEmail(email);
    	if(customer.isEmpty()) return null;
    	
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customer.get().getId());
        if (loans != null) {
            return loans;
        } else {
            return null;
        }
    }

}
