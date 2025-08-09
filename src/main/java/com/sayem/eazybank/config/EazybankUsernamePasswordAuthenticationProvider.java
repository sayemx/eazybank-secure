/* Comenting in section 15 as we are making this as a oauth2 resource server
 * and the authenitcation wwil depend on the auth server - in our  case keyclock. 
 *
package com.sayem.eazybank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("!prod")
@Component
public class EazybankUsernamePasswordAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public EazybankUsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String providedPassword = authentication.getCredentials().toString();
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		// more business logics an be written
//		if(passwordEncoder.matches(providedPassword, userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userDetails, providedPassword, userDetails.getAuthorities());
//		}else {
//			throw new BadCredentialsException("Invalid Password");
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
*/
