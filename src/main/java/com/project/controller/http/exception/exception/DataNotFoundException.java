package com.project.controller.http.exception.exception;

public final class DataNotFoundException extends ControllerException {

	public DataNotFoundException(ExceptionSubject subject) { super(subject); }

	@Override public int getCode() { return 1; }
}
