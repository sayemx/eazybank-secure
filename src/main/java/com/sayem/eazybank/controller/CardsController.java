package com.sayem.eazybank.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.eazybank.entity.Cards;
import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.CardsRepository;
import com.sayem.eazybank.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private final CardsRepository cardsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myCardsOld")
    public List<Cards> getCardDetailsById(@RequestParam long id) {
        List<Cards> cards = cardsRepository.findByCustomerId(id);
        if (cards != null ) {
            return cards;
        }else {
            return null;
        }
    }
    
    @GetMapping("/myCards")
    public List<Cards> getCardDetailsByEmail(@RequestParam String email) {
    	Optional<Customer> customer = customerRepository.findByEmail(email);
    	if(customer.isEmpty()) return null;
    	
        List<Cards> cards = cardsRepository.findByCustomerId(customer.get().getId());
        if (cards != null ) {
            return cards;
        }else {
            return null;
        }
    }

}
