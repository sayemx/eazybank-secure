package com.sayem.eazybank.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationEvent {
	
	/*
	 * Since we have provided our own AuthenticationProvider
	 * this won't triggered unless we publish event from our 
	 * custom provider.
	 */
	
	
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		log.info("\nLogin Success with an event {}", event.getAuthentication().getName());
	}
	
	@EventListener
	public void onFailure(AbstractAuthenticationFailureEvent event) {
		log.error("\nLogin Failure with an event {}", event.getAuthentication().getName());
	}
}
