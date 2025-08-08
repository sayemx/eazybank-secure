package com.sayem.eazybank.controller;

import java.sql.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sayem.eazybank.constants.ApplicationContants;
import com.sayem.eazybank.dto.LoginRequestDto;
import com.sayem.eazybank.dto.LoginResponseDto;
import com.sayem.eazybank.entity.Customer;
import com.sayem.eazybank.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            customer.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomer = customerRepository.save(customer);

            if (savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given user details are successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }
    }
    
    @GetMapping("/welcome")
    public String get() {
    	return "Welcome";
    }

    // this is responsible for login with basic authentication
    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }
    
    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto){
    	
    	String jwtToken = "";
    	Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.username(), loginRequestDto.password());
    	
    	Authentication authenticateRes = authenticationManager.authenticate(authentication);
    	if(authenticateRes != null && authenticateRes.isAuthenticated()) {
    		// generate jwt token 
			if(env != null) {
				String secret = env.getProperty(ApplicationContants.JWT_SECRET_KEY, ApplicationContants.JWT_SECRET_DEFAULT_VALUE);
				jwtToken = JWT.create()
					.withIssuer("Eazy Bank")
	                .withSubject("JWT Token")
	                .withClaim("username", authenticateRes.getName())
	                .withClaim("authorities", authenticateRes.getAuthorities().stream().map(
	                		GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
	                .withIssuedAt(new java.util.Date())
	                .withExpiresAt(new Date(System.currentTimeMillis() + ApplicationContants.JWT_EXPIRATION))
	                .sign(Algorithm.HMAC256(secret));
			}
    		
    	}
    	return ResponseEntity.status(HttpStatus.OK).header(ApplicationContants.JWT_HEADER, jwtToken)
    			.body(new LoginResponseDto(HttpStatus.OK, jwtToken));
    }
    
    

}
