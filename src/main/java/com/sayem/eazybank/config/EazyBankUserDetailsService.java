package com.sayem.eazybank.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.CustomerRepository;

@Service
public class EazyBankUserDetailsService implements UserDetailsService{
	
	@Autowired
	CustomerRepository customerrepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Customer user = customerrepository.findByEmail(username).orElseThrow(() -> new
				UsernameNotFoundException("User not found"));
		
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
		return new User(user.getEmail(), user.getPwd(), authorities);
	}

}
