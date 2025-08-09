/* Comenting in section 15 as we are making this as a oauth2 resource server
 * and the authenitcation wwil depend on the auth server - in our  case keyclock. 
 *
package com.sayem.eazybank.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sayem.eazybank.entity.Authority;
import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.CustomerRepository;

@Service
public class EazyBankUserDetailsService implements UserDetailsService{
	
	@Autowired
	CustomerRepository customerRepository;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
//		Customer user = customerRepository.findByEmail(username).orElseThrow(() -> new
//				UsernameNotFoundException("User not found"));
//		
////		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
//		
//		Set<Authority> authorities = user.getAuthorities();
//		List<GrantedAuthority> authoritiesList = authorities
//				.stream()
//				.map(authority -> new SimpleGrantedAuthority(authority.getName()))
//				.collect(Collectors.toList());
//		
//		
//		
//		return new User(user.getEmail(), user.getPwd(), authoritiesList);
//	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new
                UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> authorities = customer.getAuthorities().stream().map(authority -> new
                        SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }


}
*/