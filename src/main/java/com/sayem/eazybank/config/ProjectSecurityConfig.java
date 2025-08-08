package com.sayem.eazybank.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.sayem.eazybank.exception.CustomAccessDeniedHandler;
import com.sayem.eazybank.exception.CustomBasicAuthenitcationEntryPoint;
import com.sayem.eazybank.filter.AuthoritiesLoggingAfterFilter;
import com.sayem.eazybank.filter.AuthoritiesLoggingAtFilter;
import com.sayem.eazybank.filter.CsrfCookieFilter;
import com.sayem.eazybank.filter.JwtTokenGeneratorFilter;
import com.sayem.eazybank.filter.JwtTokenValidatorFilter;
import com.sayem.eazybank.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {
	
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		// permitAll(), denyAll(), authenticated()
		
		// to manage session fixation
		//http.sessionManagement(smc -> smc.sessionFixation(sfc -> sfc.none()));
		
		CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		
		
		http
			//.securityContext(contextConfig -> contextConfig.requireExplicitSave(false)) // no need anymore as we won't use JSESSIONID or cookie
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
			
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(Arrays.asList("Authorization"));
					config.setAllowCredentials(true);
					config.setMaxAge(3600L);
					
					return config;
				}
			}))
			.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession")
					.maximumSessions(10)
					.maxSessionsPreventsLogin(true)
					.expiredUrl("/expired"))
	        .csrf(csrf -> csrf
	        		.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
	        		.ignoringRequestMatchers("/contact", "/register", "/apiLogin")
	        		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
	        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
	        .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
	        .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
	        .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
	        .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
	        .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
	        .authorizeHttpRequests(requests -> requests // [VIEWLOANS, VIEWACCOUNT, VIEWBALANCE, VIEWCARDS]
	            //.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans", "/user").authenticated()
	            /*.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
	            .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
	            .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
	            .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")*/
        		.requestMatchers("/myAccount").hasRole("USER")
	            .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
	            .requestMatchers("/myCards").hasRole("USER")
	            .requestMatchers("/myLoans").hasRole("USER")
	            .requestMatchers("/user").authenticated()
	            .requestMatchers("/register", "/notices", "/contact", "/error", "/invalidSession", "/expired", "/apiLogin").permitAll()
	        )
	        .formLogin(withDefaults())
	        .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenitcationEntryPoint()));
		
		http.exceptionHandling(exh -> exh.accessDeniedHandler(new CustomAccessDeniedHandler()));
		
		
		// for global handling
		// http.exceptionHandling(exh -> exh.authenticationEntryPoint(new CustomBasicAuthenitcationEntryPoint()));

		return http.build();
	}
	
	/* Commenting bcz now will use JdbcUserDetailsManager
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
	
	@Bean
	public AuthenticationManager authenticatioNmanager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		
		EazybankUsernamePasswordAuthenticationProvider authenticationProvider  = new EazybankUsernamePasswordAuthenticationProvider(
				userDetailsService, passwordEncoder);
		
		ProviderManager providerManager = new ProviderManager(authenticationProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);
		
		return providerManager;
	}
	
	
}
