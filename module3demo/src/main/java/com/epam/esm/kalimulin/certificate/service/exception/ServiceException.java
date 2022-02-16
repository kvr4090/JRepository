package com.epam.esm.kalimulin.certificate.service.exception;

/**
 * Signals that have some problem with processing data, or data access.
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
