package com.epam.esm.kalimulin.certificate.controller.exception;

public class JwtException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public JwtException() {
    }

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtException(Throwable cause) {
        super(cause);
    }

}
