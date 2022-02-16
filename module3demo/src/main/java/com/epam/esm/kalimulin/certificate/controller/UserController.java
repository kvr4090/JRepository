package com.epam.esm.kalimulin.certificate.controller;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ENTITY_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.util.HateoasLinkBuilder;
import com.epam.esm.kalimulin.certificate.service.UserService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@GetMapping
	public HttpEntity<List<UserClient>> getUsers(
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale)
					throws ControllerException {
		PageModel model = new PageModel(page, size);	
		try {
			List<UserClient> list = userService.getAllUsers(model);
			list = HateoasLinkBuilder.addLinksListUser(list, model, locale);
			return new ResponseEntity<>(list, OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR','USER')")
	@GetMapping("/{id}")
	public HttpEntity<UserClient> getUser(
			@PathVariable("id") int id,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		UserClient user = new UserClient(id);
		try {
			if (userService.checkAccess(user)) {
				user = userService.getUserById(user);
				user = HateoasLinkBuilder.addLinks(user, new PageModel(page, size), locale);
				return new ResponseEntity<>(user, OK);
			}			
			return new ResponseEntity<>(FORBIDDEN);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		} catch (EntityNotFoundException e) {
			throw new ControllerException(locale, NOT_FOUND, ENTITY_NOT_FOUND, id);
		}
	}
}