package com.project.config;

import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import com.project.controller.http.exception.exception.DataNotFoundException;
import com.project.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

	private final UserRepository repository;

	@Bean public UserDetailsService userDetailsService() { return email -> repository.findByEmail(email).orElseThrow(() -> new DataNotFoundException(ExceptionSubject.USERNAME)); }
	@Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

	@Bean public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
