package com.epam.esm.kalimulin.certificate.service.exception;

public class EntityNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

}
