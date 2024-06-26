package com.project.controller.http.exception.handler;

import com.google.zxing.WriterException;
import com.project.controller.http.data.response.ResponseBuilder;
import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

import static com.project.utils.Utils.TestUtils.printSeparator;

@ControllerAdvice
public class ServerExceptionHandler {

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<Object> handle(MessagingException ex) {
		printSeparator(); ex.printStackTrace(System.out);
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseBuilder.reject(status,0,ExceptionSubject.MAIL,null);
	}

	@ExceptionHandler(WriterException.class)
	public ResponseEntity<Object> handle(WriterException ex) {
		printSeparator(); ex.printStackTrace(System.out);
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseBuilder.reject(status,0,ExceptionSubject.QR,null);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Object> handle(IOException ex) {
		printSeparator(); ex.printStackTrace(System.out);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseBuilder.reject(status,0,ExceptionSubject.IO,null);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleException(RuntimeException ex) {
		printSeparator();ex.printStackTrace(System.out);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseBuilder.reject(status,0,null,null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {
		printSeparator(); ex.printStackTrace(System.out);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseBuilder.reject(status,0,null,null);
	}
}
