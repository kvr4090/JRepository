package com.epam.esm.kalimulin.certificate.dao.impl;

import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.FIND_CERTIFICATE_BY_TAGS;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.TAGS;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.COUNT;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.DELETE_CERTIFICATE;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.LIST_ID;
import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.FIND_CERTIFICATE_BY_LIST_ID;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.esm.kalimulin.certificate.dao.CertificateDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.dao.util.Resources;
import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;

@Repository
public class CertificateDaoImpl implements CertificateDao {

	private Session session;
	
	@Autowired
	public CertificateDaoImpl(EntityManager entityManager, Session session) {
		this.session = session;
	}

	@Override
	public List<Certificate> getAllCertificates(PageModel model) throws DaoException {
		try {
			Criteria criteria = session.createCriteria(Certificate.class);
			criteria.setFirstResult(model.getPage());
			criteria.setMaxResults(model.getSize());
			criteria.addOrder(org.hibernate.criterion.Order.asc(ID));
			return criteria.list();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Certificate addCertificate(Certificate certificate) throws DaoException {
		saveOrUpdateCertificate(certificate);
		return certificate;
	}

	@Override
	public Certificate findCertificateById(Certificate certificate) throws DaoException {
		try {
			return session.get(Certificate.class, certificate.getId());
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Certificate> findCertificateByParams(Certificate certificate, PageModel model) throws DaoException {
		try {
			List<Certificate> list = session.createQuery(Resources.getQueryString(FIND_CERTIFICATE_BY_TAGS))
					.setParameterList(TAGS, certificate.getTags())
					.setParameter(COUNT, Long.valueOf(certificate.getTags().size()))
					.list();
			return list;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteCertificate(Certificate certificate) throws DaoException {
		try {
			Query query = session.createQuery(Resources.getQueryString(DELETE_CERTIFICATE));
			query.setParameter(ID, certificate.getId());
			query.executeUpdate();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Certificate updateCertificate(Certificate certificate) throws DaoException {
		saveOrUpdateCertificate(certificate);
		return certificate;
	}
	
	private void saveOrUpdateCertificate(Certificate certificate) throws DaoException {
		try {
			session.saveOrUpdate(certificate);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Certificate> findCertificateById(List<Certificate> listId) throws DaoException {
		try {
			return session.createQuery(FIND_CERTIFICATE_BY_LIST_ID).setParameterList(LIST_ID, listId).list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
