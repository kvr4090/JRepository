package com.epam.esm.kalimulin.certificate.controller.util;

public class ExceptionHandlerDTO {
	
	private String message;
	private int code;

	public ExceptionHandlerDTO() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String errorMessage) {
		this.message = errorMessage;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int errorCode) {
		this.code = errorCode;
	}
}
