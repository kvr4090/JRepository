package com.epam.esm.kalimulin.certificate.controller;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.DISABLED;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.util.JwtResponse;
import com.epam.esm.kalimulin.certificate.service.UserService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UserService userService;

	@Autowired
	public AuthController(UserService userService) {
		this.userService = userService;
	}

    @PostMapping("/signin")
    public HttpEntity<JwtResponse> authenticateUser(
    		@RequestBody User user,
    		@RequestParam(required = true, defaultValue = "${default.language}") String locale)
    				throws ControllerException {
        try {
			return new ResponseEntity<>(userService.authUser(user), OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		} catch (DisabledException exception) {
			throw new ControllerException(locale, FORBIDDEN, DISABLED, exception);
		} catch (EntityNotFoundException exception) {
			throw new ControllerException(locale, OK, exception.getMessage(), exception);
		}
    }
    
    @PostMapping("/signup")
    public HttpEntity<User> registrationUser(
    		@RequestBody User user,
    		@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
    				throws ControllerException {
    	try {
    		return new ResponseEntity<>(userService.saveUser(user), CREATED);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
    }
}
