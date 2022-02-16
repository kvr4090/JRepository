package com.epam.esm.kalimulin.certificate.dao.impl;

import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.USERNAME;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.FIND_USER_BY_NAME;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.dao.UserDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.dao.util.Resources;

@Repository
public class UserDaoImpl implements UserDao {

	private Session session;

	@Autowired
	public UserDaoImpl(EntityManager entityManager, Session session) {
		this.session = session;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<UserClient> getAllUsers(PageModel model) throws DaoException {
		try {
			Criteria criteria = session.createCriteria(UserClient.class);
			criteria.setFirstResult(model.getPage());
			criteria.setMaxResults(model.getSize());
			criteria.addOrder(Order.asc(ID));
			List<UserClient> resultPage = criteria.list();
			return resultPage;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public UserClient getUserById(UserClient user) throws DaoException {
		try {
			return session.get(UserClient.class, user.getId());
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User getUserByName(User user) throws DaoException {
		try {
			List<User> users = session.createQuery(Resources.getQueryString(FIND_USER_BY_NAME))
					.setParameter(USERNAME, user.getUsername())
					.list();
			if (users.size() > 0) {
				return users.get(0);
			}
			return null;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public User addUser(User user) throws DaoException {
		try {
			session.save(user);
		} catch (DataAccessException | PersistenceException e) {
			System.out.println("catched");
		}
		return user;
	}

	@Override
	public List<UserClient> findAll() {
		return null;
	}

	@Override
	public List<UserClient> findAll(Sort sort) {
		return null;
	}

	@Override
	public List<UserClient> findAllById(Iterable<Integer> ids) {
		return null;
	}

	@Override
	public <S extends UserClient> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public void flush() {		
	}

	@Override
	public <S extends UserClient> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends UserClient> List<S> saveAllAndFlush(Iterable<S> entities) {
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<UserClient> entities) {	
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {		
	}

	@Override
	public void deleteAllInBatch() {
	}

	@Override
	public UserClient getOne(Integer id) {
		return null;
	}

	@Override
	public UserClient getById(Integer id) {
		return null;
	}

	@Override
	public <S extends UserClient> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends UserClient> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public Page<UserClient> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public <S extends UserClient> S save(S entity) {
		return null;
	}

	@Override
	public Optional<UserClient> findById(Integer id) {
		return null;
	}

	@Override
	public boolean existsById(Integer id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Integer id) {	
	}

	@Override
	public void delete(UserClient entity) {	
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {	
	}

	@Override
	public void deleteAll(Iterable<? extends UserClient> entities) {
	}

	@Override
	public void deleteAll() {	
	}

	@Override
	public <S extends UserClient> Optional<S> findOne(Example<S> example) {
		return null;
	}

	@Override
	public <S extends UserClient> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends UserClient> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends UserClient> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends UserClient, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}
}
