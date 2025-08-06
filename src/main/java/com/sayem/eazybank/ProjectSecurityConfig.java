package com.sayem.eazybank;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
	
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		// permitAll(), denyAll(), authenticated()
		
		http.authorizeHttpRequests((requests) -> requests.
				requestMatchers("/myAccount", "myBalance", "/myCards", "myLoans").authenticated()
				.requestMatchers("/notices", "/contact", "/error").permitAll());
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}
	
}
