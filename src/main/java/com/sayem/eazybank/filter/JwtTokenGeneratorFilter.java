package com.sayem.eazybank.filter;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sayem.eazybank.constants.ApplicationContants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			Environment env = getEnvironment();
			if(env != null) {
				String secret = env.getProperty(ApplicationContants.JWT_SECRET_KEY, ApplicationContants.JWT_SECRET_DEFAULT_VALUE);
				String jwtToken = JWT.create()
					.withIssuer("Eazy Bank")
	                .withSubject("JWT Token")
	                .withClaim("username", authentication.getName())
	                .withClaim("authorities", authentication.getAuthorities().stream().map(
	                		GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
	                .withIssuedAt(new Date())
	                .withExpiresAt(new Date(System.currentTimeMillis() + ApplicationContants.JWT_EXPIRATION))
	                .sign(Algorithm.HMAC256(secret));
				
				response.setHeader(ApplicationContants.JWT_HEADER, jwtToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/user");
	}

}
