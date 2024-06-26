package com.project.controller.http.exception.handler;

import com.project.controller.http.data.response.ResponseBuilder;
import com.project.controller.http.exception.exception.AuthenticationException;
import com.project.controller.http.exception.exception.ControllerException;
import com.project.controller.http.exception.exception.DataAlreadyRegisteredException;
import com.project.controller.http.exception.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see DataNotFoundException 1 - Data Not Found
 * @see DataAlreadyRegisteredException 2 - Already Registered
 * @see AuthenticationException 3 - Authentication
 **/
@ControllerAdvice()
public class DefaultExceptionHandler {

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<Object> handle(ControllerException ex) {
		return ResponseBuilder.reject(HttpStatus.NOT_IMPLEMENTED,ex.getCode(), ex.getSubject(),null);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handle(DataNotFoundException ex) {
		return ResponseBuilder.reject(HttpStatus.NOT_FOUND,ex.getCode(), ex.getSubject(),null);
	}

	@ExceptionHandler(DataAlreadyRegisteredException.class)
	public ResponseEntity<Object> handle(DataAlreadyRegisteredException ex) {
		return ResponseBuilder.reject(HttpStatus.BAD_REQUEST,ex.getCode(), ex.getSubject(),null);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handle(AuthenticationException ex) {
		return ResponseBuilder.reject(HttpStatus.UNAUTHORIZED,ex.getCode(), ex.getSubject(),null);
	}

}
