package com.epam.esm.kalimulin.certificate.service.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
