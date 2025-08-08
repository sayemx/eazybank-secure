package com.sayem.eazybank.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationEevents {
	
	@EventListener
	public void onAuthorizationFailure(AuthorizationDeniedEvent event) {
		log.error("Authorization Failed : {}, {}", event.getAuthentication().get().getName(), event.getAuthorizationDecision().toString());
	}
}
