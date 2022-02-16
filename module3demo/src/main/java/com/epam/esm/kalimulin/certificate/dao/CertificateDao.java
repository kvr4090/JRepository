package com.epam.esm.kalimulin.certificate.dao;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

public interface CertificateDao {
	
	List<Certificate> getAllCertificates(PageModel model) throws DaoException;
	
	Certificate addCertificate(Certificate certificate) throws DaoException;
		
	Certificate findCertificateById(Certificate certificate) throws DaoException;
	
	List<Certificate> findCertificateById(List<Certificate> listId) throws DaoException;
	
	List<Certificate> findCertificateByParams(Certificate certificate, PageModel model) throws DaoException;
	
	void deleteCertificate(Certificate certificate) throws DaoException;
	
	Certificate updateCertificate(Certificate certificate) throws DaoException;
}
