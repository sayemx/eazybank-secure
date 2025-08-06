package com.sayem.eazybank.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class ProjectSecurityConfig {
	
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		// permitAll(), denyAll(), authenticated()
		http
	        .csrf(csrf -> csrf.disable()) // Disable CSRF for POST to work on public endpoints
	        .authorizeHttpRequests(requests -> requests
	            .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
	            .requestMatchers("/registerUser", "/notices", "/contact", "/error").permitAll()
	        )
	        .formLogin(withDefaults())
	        .httpBasic(withDefaults());

		return http.build();
	}
	
	/* Commenting bcz now will use JdbcUserDetailsProvier
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails sayem1 = User.withUsername("sayem1").password("{noop}sayem@123321").authorities("read").build();
		UserDetails admin1 = User.withUsername("admin1").password("{noop}admin123").authorities("write").build();
		
		UserDetails sayem2 = User.withUsername("sayem2").password("{bcrypt}$2a$12$wMABk4zbNj3bjfYq8nz5y.zE/.k2bJtJJUBf7k7osZ9Jx.KTbrg5O").authorities("read").build();
		UserDetails admin2 = User.withUsername("admin2").password("{bcrypt}$2a$12$bEJZD4oHxBheF4vM4aT7aeVjZLhy9VOKcAiJfDbB0DXUKcdjdKD5S").authorities("write").build();
		return new InMemoryUserDetailsManager(sayem1, admin1, sayem2, admin2);
	}*/
	
	/* Commenting bcz we defined out own UserDetailsService impl
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
	
}
