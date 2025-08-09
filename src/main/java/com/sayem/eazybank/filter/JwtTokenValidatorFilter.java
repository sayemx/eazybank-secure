/* Comenting in section 15 as we are making this as a oauth2 resource server
 * and the authenitcation wwil depend on the auth server - in our  case keyclock. 
 *
package com.sayem.eazybank.filter;

import java.io.IOException;
import java.util.Date;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sayem.eazybank.constants.ApplicationContants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwtToken = request.getHeader(ApplicationContants.JWT_HEADER);
		if(jwtToken != null) {
			try {
				Environment env = getEnvironment();
				if(env != null) {
					
					String secret = env.getProperty(ApplicationContants.JWT_SECRET_KEY, ApplicationContants.JWT_SECRET_DEFAULT_VALUE);
					DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
					        .withIssuer("Eazy Bank") // optional but helps validate
					        .build()
					        .verify(jwtToken); // throws exception if token is invalid
					
					String username = decodedJWT.getClaim("username").asString();
					String authorities = decodedJWT.getClaim("authorities").asString();
					
					Authentication authentication = new UsernamePasswordAuthenticationToken(username,  null,
							AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				}
			}catch(Exception ex) {
				throw new BadCredentialsException("Invalid Token recieved");
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/user");
	}

}
*/