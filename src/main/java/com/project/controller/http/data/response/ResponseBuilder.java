package com.project.controller.http.data.response;

import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class ResponseBuilder {

	public static <T> ResponseEntity<T> accept(HttpStatus statusCode, String location, T data) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (location != null) headers.setLocation(URI.create(location));
		return new ResponseEntity<>(data,headers,statusCode);
	}

	public static ResponseEntity<Object> reject(HttpStatus statusCode, int errorCode, ExceptionSubject subject, String location) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (location != null) headers.setLocation(URI.create(location));
		return new ResponseEntity<>(new APIError(statusCode.value(), errorCode, subject),headers,statusCode);
	}

	private record APIError(int statusCode, int errorCode, ExceptionSubject subject) { }
}
