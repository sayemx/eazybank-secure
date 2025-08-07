package com.sayem.eazybank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.Accounts;
import com.sayem.eazybank.repository.AccountsRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountsRepository accountsRepository;

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam long id) {
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }

}
