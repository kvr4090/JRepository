package com.epam.esm.kalimulin.certificate.service;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

public interface CertificateService {

	Certificate addCertificate(Certificate certificate) throws ServiceException, ValidationException;

	List<Certificate> findCertificate(List<Long> tagsId, PageModel model)
			throws ServiceException, ValidationException;
	
	Certificate findCertificateById(Certificate certificate) 
			throws ServiceException, ValidationException, EntityNotFoundException;
	
	List<Certificate> findCertificateById(List<Certificate> list) throws ServiceException, ValidationException;

	String deleteCertificate(Certificate certificate) 
			throws ServiceException, ValidationException, EntityNotFoundException;

	Certificate updateCertificate(Certificate certificate) throws ServiceException, ValidationException;
	
}
