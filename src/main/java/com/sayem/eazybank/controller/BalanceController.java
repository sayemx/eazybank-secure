package com.sayem.eazybank.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.AccountTransactions;
import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.AccountTransactionsRepository;
import com.sayem.eazybank.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final AccountTransactionsRepository accountTransactionsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myBalanceOld")
    public List<AccountTransactions> getBalanceDetailsById(@RequestParam long id) {
        List<AccountTransactions> accountTransactions = accountTransactionsRepository.
                findByCustomerIdOrderByTransactionDtDesc(id);
        if (accountTransactions != null) {
            return accountTransactions;
        } else {
            return null;
        }
    }
    
    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetailsByEmail(@RequestParam String email) {
    	Optional<Customer> customer = customerRepository.findByEmail(email);
    	if(customer.isEmpty()) return null;
    	
        List<AccountTransactions> accountTransactions = accountTransactionsRepository.
                findByCustomerIdOrderByTransactionDtDesc(customer.get().getId());
        if (accountTransactions != null) {
            return accountTransactions;
        } else {
            return null;
        }
    }
}
