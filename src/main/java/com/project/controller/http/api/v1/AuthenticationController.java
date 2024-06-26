package com.project.controller.http.api.v1;

import com.project.config.security.jwt.JWTAuthFilter;
import com.project.controller.http.data.request.user.AuthenticationLoginRequest;
import com.project.controller.http.data.request.user.client.ClientAuthenticationRegisterRequest;
import com.project.controller.http.data.request.user.client.ClientLoginRequest;
import com.project.controller.http.data.response.ResponseBuilder;
import com.project.model.dto.EntityDTO;
import com.project.model.enums.UserRole;
import com.project.model.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<EntityDTO> register(@RequestBody ClientAuthenticationRegisterRequest request) {
		return ResponseBuilder.accept(HttpStatus.CREATED,null,service.register(request,UserRole.ADMIN));
	}

	@PostMapping("/login")
	public ResponseEntity<EntityDTO> login(@RequestBody ClientLoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		EntityDTO dto = service.login(new AuthenticationLoginRequest(request.email(), request.password()));

		var cookie = new Cookie("jwt-token",servletRequest.getHeader(JWTAuthFilter.AUTHORIZATION_HEADER));
		cookie.setMaxAge(7 * 24 * 60 * 60);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		servletResponse.addCookie(cookie);

		return ResponseBuilder.accept(HttpStatus.OK,null,dto);
	}
}
