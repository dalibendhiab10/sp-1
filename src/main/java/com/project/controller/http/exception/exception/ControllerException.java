package com.project.controller.http.exception.exception;

/**
 * @see DataNotFoundException 1 - Data Not Found
 * @see DataAlreadyRegisteredException 2 - Already Registered
 * @see AuthenticationException 3 - Authentication
 * @see QRInputException 4 - QR Wrong Input
 * @see QRNoContentException 5 - QR No Content
 **/
public class ControllerException extends RuntimeException {

	public enum ExceptionSubject { ID, USERNAME, IO, QR, MAIL, PASSWORD, JWT }

	private final ExceptionSubject subject;

	public ControllerException(ExceptionSubject subject) { this.subject = subject; }

	public int getCode() { return 0; }
	public ExceptionSubject getSubject() { return subject; }
}
