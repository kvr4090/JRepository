package com.epam.esm.kalimulin.certificate.dao.impl;

import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.USER;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ALIAS_ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ALIAS;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.dao.OrderDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

@Repository
public class OrderDaoImpl implements OrderDao {
	
	private Session session;

	@Autowired
	public OrderDaoImpl(EntityManager entityManager, Session session) {
		this.session = session;
		entityManager.unwrap(Session.class);
	}
	
	@Override
	public List<Order> findOrderByUserId(Order order, PageModel model) throws DaoException {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Order.class);
			dc.createAlias(USER, ALIAS);
			dc.add(Restrictions.in(ALIAS_ID , new Object[]{order.getUser().getId()}));
			dc.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			dc.setProjection(Projections.id());

			Criteria outer = session.createCriteria(Order.class);
			outer.add(Subqueries.propertyIn(ID, dc));
			outer.setFirstResult(model.getPage());
			outer.setMaxResults(model.getSize());
			return outer.list();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findAllOrders(PageModel model) throws DaoException {
		try {
			Criteria criteria = session.createCriteria(Order.class);
			criteria.setFirstResult(model.getPage());
			criteria.setMaxResults(model.getSize());
			criteria.addOrder(org.hibernate.criterion.Order.asc(ID));
			List<Order> resultPage = criteria.list();
			return resultPage;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public Order addOrder(Order order) throws DaoException {
		try {
			session.saveOrUpdate(order);
			return order;	
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Order> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Order> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Order> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Order> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Order> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Order> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Order> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Order, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}
}
