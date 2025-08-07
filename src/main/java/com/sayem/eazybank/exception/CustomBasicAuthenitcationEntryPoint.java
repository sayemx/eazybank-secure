package com.sayem.eazybank.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomBasicAuthenitcationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		LocalDateTime currentTimeStamp = LocalDateTime.now();
		String message = (authException != null && authException.getMessage() != null) ? authException.getMessage()
				: "Unauthorize";
		String path = request.getServletPath();
		
		response.setHeader("eazybank-error-reason", "Authentication Failed");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		
		String json = "{\n" +
			    "    \"timestamp\": \"%s\",\n" +
			    "    \"status\": %d,\n" +
			    "    \"error\": \"%s\",\n" +
			    "    \"message\": \"%s\",\n" +
			    "    \"path\": \"%s\"\n" +
			"}";
		
		String formattedJson = String.format(json,
				currentTimeStamp, 
			    401,                             
			    "Unauthorized",                  
			    message,                  
			    path
			);
		
		response.getWriter().write(formattedJson);
		
	}

}
