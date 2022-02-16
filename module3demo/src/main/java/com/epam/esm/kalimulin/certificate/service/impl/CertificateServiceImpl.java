package com.epam.esm.kalimulin.certificate.service.impl;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.SEVERE_PROBLEM;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.DELETED;

import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.kalimulin.certificate.service.CertificateService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;
import com.epam.esm.kalimulin.certificate.service.util.AppValidator;
import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.dao.CertificateDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

@Service
public class CertificateServiceImpl implements CertificateService {
	
	private final CertificateDao certificateDao;														
	private final AppValidator validator;
	
	@Autowired
	public CertificateServiceImpl(CertificateDao certificateDao, AppValidator validator) {
		this.certificateDao = certificateDao;
		this.validator = validator;
	}

	@Override
	public Certificate addCertificate(Certificate certificate) 
			throws ServiceException, ValidationException {
		validator.validate(certificate);
		try {
			return certificateDao.addCertificate(certificate);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} 					
	}
	
	@Override
	@Transactional
	public List<Certificate> findCertificate(List<Long> tagsId, PageModel model)
			throws ServiceException, ValidationException {
		try {
			System.out.println("do smth");
			if (Objects.isNull(tagsId)) {
				return getAllCertificates(model);
			}
			Certificate certificate = new Certificate();
			certificate.setTags(formListTagById(tagsId));
			return findCertificateWithParams(certificate, model);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	private List<Certificate> getAllCertificates(PageModel model) 
			throws DaoException, ValidationException {
		validator.validate(model);
		return certificateDao.getAllCertificates(model);
	}
	
	@Override
	public Certificate findCertificateById(Certificate certificate) 
			throws ValidationException, ServiceException, EntityNotFoundException {
		validator.validate(certificate.getId());
		try {
			certificate = certificateDao.findCertificateById(certificate);
			if (certificate == null) {
				throw new EntityNotFoundException();
			}
			return certificate;
		} catch (DaoException e) {
			throw new ServiceException(SEVERE_PROBLEM, e);
		}
	}
	
	private List<Certificate> findCertificateWithParams(Certificate certificate, PageModel model) 
			throws DaoException, ServiceException, ValidationException {
		validator.validate(certificate.getTags());
		return certificateDao.findCertificateByParams(certificate, model);
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public String deleteCertificate(Certificate certificate) 
			throws ServiceException, ValidationException, EntityNotFoundException {
		validator.validate(certificate.getId());
		try {
			certificate = findCertificateById(certificate);
			certificateDao.deleteCertificate(certificate);
			return DELETED;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}					
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public Certificate updateCertificate(Certificate certificate) 
			throws ServiceException, ValidationException {
		validator.validateForUpdate(certificate);
		try {
			certificate.setLastUpdateDate(Instant.now());			
			Certificate oldCertificate = certificateDao.findCertificateById(certificate);
			copyNonNullProperties(certificate, oldCertificate);
			return certificateDao.updateCertificate(oldCertificate);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}	
	}

	private void copyNonNullProperties(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	private String[] getNullPropertyNames (Object source) {
		
	    final BeanWrapper src = new BeanWrapperImpl(source);    
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    Set<String> emptyNames = new HashSet<String>();
	    for (PropertyDescriptor pd : pds) {    	
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null || srcValue.equals(0)) {        	
	        	emptyNames.add(pd.getName());
	        }
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
	private List<Tag> formListTagById(List<Long> listId) {
		List<Tag> result = new ArrayList<>();
		for (Long id : listId) {
			Tag temp = new Tag();
			temp.setId(id);
			result.add(temp);
		}
		return result;
	}

	@Override
	public List<Certificate> findCertificateById(List<Certificate> list) throws ServiceException, ValidationException {
		validator.validateId(list);
		try {
			return certificateDao.findCertificateById(list);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
