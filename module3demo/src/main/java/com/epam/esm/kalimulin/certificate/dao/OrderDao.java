package com.epam.esm.kalimulin.certificate.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
	
	Order addOrder(Order order) throws DaoException;
	
	List<Order> findOrderByUserId(Order order, PageModel model) throws DaoException;
	
	List<Order> findAllOrders(PageModel model) throws DaoException;
}
