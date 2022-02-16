package com.epam.esm.kalimulin.certificate.controller.exception;

import org.springframework.http.HttpStatus;

public class ControllerException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String locale;
	private HttpStatus status;
	private int beanId;

	public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }
    
    public ControllerException(String locale, HttpStatus status, String message, Throwable cause) {
    	super(message, cause);
    	this.locale = locale;
    	this.status = status;
    }
    
    public ControllerException(String locale, HttpStatus status, String message, int beanId) {
    	super(message);
    	this.locale = locale;
    	this.status = status;
    	this.beanId = beanId;
    }
    
    public ControllerException(String locale, HttpStatus status, Throwable cause) {
    	super(cause);
    	this.locale = locale;
    	this.status = status;
    }

	public String getLocale() {
		return locale;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public int getBeanId() {
		return beanId;
	}
}
