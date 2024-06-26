package com.project.config.security;

import com.project.config.security.jwt.JWTAuthFilter;
import com.project.model.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration @EnableWebSecurity
public class WebSecurityConfig {

	private final JWTAuthFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request
						.requestMatchers("/api/v*/test/**").permitAll()
						.requestMatchers("/api/v*/auth/**").permitAll()//.anonymous()
						.requestMatchers("/api/v*/admin/**").hasAuthority(UserRole.ADMIN.name())//hasRole(UserRole.ADMIN.name())
						.requestMatchers("/api/v*/client/**").permitAll()//.hasAnyAuthority(UserRole.ADMIN.name(),UserRole.CLIENT.name())
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
