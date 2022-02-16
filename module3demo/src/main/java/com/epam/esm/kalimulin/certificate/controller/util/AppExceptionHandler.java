package com.epam.esm.kalimulin.certificate.controller.util;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.LOCALE_SOURCE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ADDITIONAL_ERROR_CODE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ENG;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.RU;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.SEVERE_PROBLEM;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.exception.JwtException;

@ControllerAdvice
public class AppExceptionHandler {
	
	private final Locale engLocale = new Locale(ENG);
	private final Locale ruLocale = new Locale(RU);
	private final ResourceBundle enResBundle = ResourceBundle.getBundle(LOCALE_SOURCE, engLocale);
	private final ResourceBundle ruResBundle = ResourceBundle.getBundle(LOCALE_SOURCE, ruLocale);

	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(ControllerException exception) {
		ExceptionHandlerDTO data = new ExceptionHandlerDTO();
		data.setMessage(getLocalizedMessage(exception));
        data.setCode(exception.getStatus().value() + ADDITIONAL_ERROR_CODE);
        if (exception.getBeanId() != 0) {
			data.setMessage(data.getMessage() + exception.getBeanId());
		}
        return new ResponseEntity<>(data, exception.getStatus());
    }

	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(Throwable throwable) {
		ExceptionHandlerDTO data = new ExceptionHandlerDTO();
		data.setMessage(getLocalizedMessage(SEVERE_PROBLEM, ENG) + throwable.getCause());
        data.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + ADDITIONAL_ERROR_CODE);     
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(AccessDeniedException exception) {
		ExceptionHandlerDTO data = new ExceptionHandlerDTO();
		data.setMessage(exception.getLocalizedMessage());
        data.setCode(HttpStatus.FORBIDDEN.value() + ADDITIONAL_ERROR_CODE);     
        return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
    }
	
	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(JwtException exception) {
		return badRequest();
    }
	
	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(MethodArgumentTypeMismatchException exception) {
		return badRequest();
    }
	
	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(MissingServletRequestParameterException exception) {
		return badRequest();
    }
	
	@ExceptionHandler
    public ResponseEntity<ExceptionHandlerDTO> handleException(HttpMessageNotReadableException exception) {
		return badRequest();
    }
	
	private ResponseEntity<ExceptionHandlerDTO> badRequest() {
		ExceptionHandlerDTO data = new ExceptionHandlerDTO();
		data.setMessage(HttpStatus.BAD_REQUEST.toString());
        data.setCode(HttpStatus.BAD_REQUEST.value() + ADDITIONAL_ERROR_CODE);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
	}

    private String getLocalizedMessage(ControllerException exception) {    	
    	return getLocalizedMessage(exception.getMessage(), exception.getLocale());
    }
    
    private String getLocalizedMessage(String message, String locale) {
    	if (locale.equals(RU)) {
			return ruResBundle.getString(message);
		}  	
    	return enResBundle.getString(message);
    }
}
