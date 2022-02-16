package com.epam.esm.kalimulin.certificate.dao.exception;

/**
 * Signals that occurred severe problem with database.
 */
public class DaoException extends Exception {

	private static final long serialVersionUID = 5189322577517829467L;

	public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
