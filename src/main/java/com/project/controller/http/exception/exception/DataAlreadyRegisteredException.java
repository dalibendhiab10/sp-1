package com.project.controller.http.exception.exception;

public final class DataAlreadyRegisteredException extends ControllerException {

	public DataAlreadyRegisteredException(ExceptionSubject subject) { super(subject); }

	@Override public int getCode() { return 2; }
}
