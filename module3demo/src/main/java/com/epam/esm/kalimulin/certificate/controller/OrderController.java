package com.epam.esm.kalimulin.certificate.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.util.HateoasLinkBuilder;
import com.epam.esm.kalimulin.certificate.service.OrderService;
import com.epam.esm.kalimulin.certificate.service.UserService;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	
	@Autowired
	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}
	
	@PostMapping
	public HttpEntity<Order> addOrder(
			@RequestBody(required = true) List<Certificate> certificates,
			@RequestParam(required = true) Long userId,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		UserClient client = new UserClient(userId);
		Order order  = new Order(client, certificates, Instant.now());
		try {
			if (userService.checkAccess(client)) {
				order = orderService.addOrder(order);
				order = HateoasLinkBuilder.addLinks(order, new PageModel(page, size), locale);
				return new ResponseEntity<>(order, HttpStatus.CREATED);
			}
			return new ResponseEntity<>(FORBIDDEN);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
	
	@GetMapping
	public HttpEntity<List<Order>> getOrder(
			@RequestParam(required = false, defaultValue = "0") Long userId,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		PageModel model = new PageModel(page, size);
		UserClient client = new UserClient(userId);
		Order order = new Order(client);
		try {
			if (userService.checkAccess(client)) {
				List<Order> result = orderService.findOrder(order, model);
				result = HateoasLinkBuilder.addLinksListOrder(result, model, locale);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			return new ResponseEntity<>(FORBIDDEN);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
}