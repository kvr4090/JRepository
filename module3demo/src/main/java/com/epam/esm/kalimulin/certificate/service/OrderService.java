package com.epam.esm.kalimulin.certificate.service;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

public interface OrderService {
	
	Order addOrder(Order order) throws ServiceException, ValidationException;
	
	List<Order> findOrder(Order order, PageModel index) throws ServiceException, ValidationException;
}
