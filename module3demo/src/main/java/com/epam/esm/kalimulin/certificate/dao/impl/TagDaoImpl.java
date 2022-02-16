package com.epam.esm.kalimulin.certificate.dao.impl;

import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.DELETE_TAG;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.POP_TAG;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.dao.TagDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.dao.util.Resources;

@Repository
public class TagDaoImpl implements TagDao {
	
	private Session session;
	
	@Autowired
	public TagDaoImpl(EntityManager entityManager, Session session) {
		this.session = session;
	}

	@Override
	public Tag addTag(Tag tag) throws DaoException {
		try {
			session.saveOrUpdate(tag);
			return tag;	
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteTag(Tag tag) throws DaoException {
		try {
			Query query = session.createQuery(Resources.getQueryString(DELETE_TAG));
			query.setParameter(ID, tag.getId());
			query.executeUpdate();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Tag findTagByName(Tag tag) throws DaoException {
		try {
			return session.get(Tag.class, tag.getName());
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public Tag findTagById(Tag tag) throws DaoException {
		try {
			return session.get(Tag.class, tag.getId());
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Tag> findAllTags(PageModel model) throws DaoException {
		try {
			Criteria criteria = session.createCriteria(Tag.class);
			criteria.setFirstResult(model.getPage());
				criteria.setMaxResults(model.getSize());
				criteria.addOrder(org.hibernate.criterion.Order.asc(ID));
			return criteria.list();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Tag findMostPopularTagFromRichestUser() throws DaoException {
		List<Object[]> asd = session.createSQLQuery(Resources.getQueryString(POP_TAG)).list();
		Tag tag = new Tag();
		tag.setId(Long.parseLong(asd.get(0)[0].toString()));
		tag.setName(asd.get(0)[1].toString());
		return tag;
	}
}
