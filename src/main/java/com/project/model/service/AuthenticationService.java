package com.project.model.service;

import com.project.config.security.jwt.JWTService;
import com.project.controller.http.data.request.user.AuthenticationLoginRequest;
import com.project.controller.http.data.request.user.AuthenticationRegisterRequest;
import com.project.controller.http.data.request.user.admin.AdminAuthenticationRegisterRequest;
import com.project.controller.http.data.request.user.client.ClientAuthenticationRegisterRequest;
import com.project.controller.http.exception.exception.AuthenticationException;
import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import com.project.controller.http.exception.exception.DataAlreadyRegisteredException;
import com.project.controller.http.exception.exception.DataNotFoundException;
import com.project.model.dao.EntityDAO;
import com.project.model.dao.UserDAO;
import com.project.model.dto.EntityDTO;
import com.project.model.enums.UserRole;
import com.project.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final AdminService adminService;
	private final ClientService clientService;

	@Override public UserDetails loadUserByUsername(String username) {
		return repository.findByEmail(username).orElseThrow(() -> new DataNotFoundException(ExceptionSubject.USERNAME));
	}

	public EntityDTO register(AuthenticationRegisterRequest request, UserRole role) {
		// Create User object
		var userDAO = new UserDAO(request.email(),passwordEncoder.encode(request.password()), role);

		// Save the object in DB
		if (repository.existsById(userDAO.getId())) throw new DataAlreadyRegisteredException(ExceptionSubject.ID);
		repository.save(userDAO);

		// Send the subtype request object to corresponding service
		EntityDAO subtypeDAO = switch (userDAO.getRole()) {
			case ADMIN -> adminService.save(userDAO, ((AdminAuthenticationRegisterRequest) request).userSubtypeRequest());
			case CLIENT -> clientService.save(userDAO, ((ClientAuthenticationRegisterRequest) request).userSubtypeRequest());
		};

		// Return the response
		return subtypeDAO.toDTO();
	}
	public EntityDTO login(AuthenticationLoginRequest request) {
		// Retrieve the user object from DB
		var dao = repository.findByEmail(request.email()).orElseThrow(() -> new AuthenticationException(ExceptionSubject.ID));

		// Authenticate user credentials with AuthenticationManager
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));

		// Retrieve the subtype user object from corresponding service
		EntityDAO entityDAO = switch (dao.getRole()) {
			case ADMIN -> adminService.get(dao.getId());
			case CLIENT -> clientService.get(dao.getId());
		};

		// Generate jwt
		var refreshToken = jwtService.generateAccessToken(dao);
		var accessToken = jwtService.generateRefreshToken(dao);

		// Save the token in JWT table in database
		jwtService.save(refreshToken, accessToken);

		// Return the response
		return entityDAO.toDTO();
	}
}
