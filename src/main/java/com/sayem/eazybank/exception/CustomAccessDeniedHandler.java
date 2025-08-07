package com.sayem.eazybank.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		
		LocalDateTime currentTimeStamp = LocalDateTime.now();
		String message = (accessDeniedException != null && accessDeniedException.getMessage() != null)
				? accessDeniedException.getMessage() : "Forbidden";
		String path = request.getServletPath();
		
		response.setHeader("eazybank-error-reason", "Forbidden");
		response.setStatus(HttpStatus.FORBIDDEN.value());
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
			    "Forbidden",                  
			    message,                  
			    path
			);
		
		response.getWriter().write(formattedJson);
		
	}

}
