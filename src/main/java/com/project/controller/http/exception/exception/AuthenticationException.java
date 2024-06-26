package com.project.controller.http.exception.exception;

public final class AuthenticationException extends ControllerException {

	public AuthenticationException(ExceptionSubject subject) { super(subject); }

	@Override public int getCode() { return 3; }
}
