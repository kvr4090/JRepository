package com.epam.esm.kalimulin.certificate.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.dao.OrderDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.service.CertificateService;
import com.epam.esm.kalimulin.certificate.service.OrderService;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;
import com.epam.esm.kalimulin.certificate.service.util.AppValidator;

@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderDao orderDao;
	private final CertificateService certificateService;
	private final AppValidator validator;
	
	@Autowired
	public OrderServiceImpl(OrderDao orderDao, AppValidator validator, CertificateService certificateService) {
		this.orderDao = orderDao;
		this.certificateService = certificateService;
		this.validator = validator;
	}

	@Override
	public Order addOrder(Order order) throws ServiceException, ValidationException {
		validator.validate(order);
		order.setPrice(summPrice(order));
		try {
			orderDao.addOrder(order);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return order;
	}
	
	private BigDecimal summPrice(Order order) throws ServiceException, ValidationException {
		BigDecimal sum = BigDecimal.ZERO;		
		for (Certificate certificate : certificateService.findCertificateById(order.getCertificates())) {
			sum = sum.add(certificate.getPrice());
		}		
		return sum;
	}
	
	@Override
	public List<Order> findOrder(Order order, PageModel model) throws ServiceException, ValidationException {
		validator.validate(model);		
		try {
			if (order.getUser().getId() == 0) {
				return orderDao.findAllOrders(model);
			}
			validator.validate(order.getUser().getId());
			return orderDao.findOrderByUserId(order, model);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
