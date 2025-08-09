/* Comenting in section 15 as we are making this as a oauth2 resource server
 * and the authenitcation wwil depend on the auth server - in our  case keyclock. 
 *
package com.sayem.eazybank.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestValidationBeforeFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String header = req.getHeader("Authorization");
		if(header != null) {
			header = header.trim();
			if(StringUtils.startsWithIgnoreCase(header, "Basic ")) {
				byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
				byte[] decoded;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
					String token = new String(decoded, StandardCharsets.UTF_8);
					int del = token.indexOf(":");
					if(del == -1) {
						throw new BadCredentialsException("Invalid basic authentication token");
					}
					
					String email = token.substring(0, del);
					if(email.toLowerCase().contains("test")) {
						res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				}catch(IllegalArgumentException ex) {
					throw new BadCredentialsException("Failed to decode basic authentication token");
				}
			}
		}
		
		chain.doFilter(request, response);

	}

}
*/
