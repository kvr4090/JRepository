package com.epam.esm.kalimulin.certificate.service.util;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.VALIDATION_PROPERTY_FILE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MAXIMUM_ID;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_ID;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_INPUT_LIST_DATA;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_PRICE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MAXIMUM_PAGE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MAXIMUM_SIZE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_PAGE_MODEL;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_NAME;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_DESCRIPTION;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_DURATION;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.REGEX_NAME;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.REGEX_DESCRIPTION;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MAXIMUM_PRICE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MINIMUM_PRICE;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.MAXIMUM_DURATION;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.REGEX_PASSWORD;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_PASSWORD;

import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@Component
public class AppValidator {
	//some str
	private final ResourceBundle validationPropBundle = ResourceBundle.getBundle(VALIDATION_PROPERTY_FILE);
	private final int MIN_PAGE = 0;
	private final int MIN_SIZE = 1;
	
	public void validate(long id) throws ValidationException {
		if ((id < 0) || (id > Long.parseLong(validationPropBundle.getString(MAXIMUM_ID)))) {
			throw new ValidationException(INVALID_ID);	
		}
	}
	
	public void validate(User user) throws ValidationException {
		if (!isCorrectName(user.getUsername())) {
			throw new ValidationException(INVALID_NAME);			
		}
		if (!isCorrectPassword(user.getPassword())) {
			throw new ValidationException(INVALID_PASSWORD);			
		}	
	}
	
	public void validate(int size) throws ValidationException {
		if (size < 1) {
			throw new ValidationException(INVALID_INPUT_LIST_DATA);
		}
	}
	
	public void validateId(List<Certificate> list) throws ValidationException {
		for (Certificate certificate : list) {
			validate(certificate.getId());
		}
	}
	
	public void validate(Order order) throws ValidationException {
		validate(order.getUser().getId());
		validate(order.getCertificates().size());
		if (!isCorrectPrice(order.getPrice())) {
			throw new ValidationException(INVALID_PRICE);
		}
	}
	
	public void validate(PageModel model) throws ValidationException {
		if (model.getPage() > Integer.parseInt(validationPropBundle.getString(MAXIMUM_PAGE)) 
				|| model.getSize() > Integer.parseInt(validationPropBundle.getString(MAXIMUM_SIZE))
				|| model.getPage() < MIN_PAGE
				|| model.getSize() < MIN_SIZE) {
			throw new ValidationException(INVALID_PAGE_MODEL);
		}
	}

	public void validate(Certificate certificate) throws ValidationException {
		
		if (!isCorrectName(certificate.getName())) {
			throw new ValidationException(INVALID_NAME);			
		}
		
		if (!isCorrectDescription(certificate.getDescription())) {
			throw new ValidationException(INVALID_DESCRIPTION);
		}
		
		if (!isCorrectDuration(certificate.getDuration())) {
			throw new ValidationException(INVALID_DURATION);
		}
		
		if (!isCorrectPrice(certificate.getPrice())) {
			throw new ValidationException(INVALID_PRICE);
		}
		
		if (certificate.getTags() != null) {
			validate(certificate.getTags());
		}
	}
	
	public void validate(Tag tag) throws ValidationException {
		validate(tag.getId());
		
		if (!isCorrectName(tag.getName())) {
			throw new ValidationException(INVALID_NAME);			
		}	
	}
	
	public void validateTagId(Tag tag) throws ValidationException {
		validate(tag.getId());
	}
	
	public void validateForUpdate(Certificate certificate) throws ValidationException {
		if (certificate.getName() != null && !isCorrectName(certificate.getName())) {
			throw new ValidationException(INVALID_NAME);
		}	
		if (!isCorrectDescription(certificate.getDescription())) {
			throw new ValidationException(INVALID_DESCRIPTION);
		}	
		if (!isCorrectDuration(certificate.getDuration())) {
			throw new ValidationException(INVALID_DURATION);
		}
		if (!isCorrectPrice(certificate.getPrice())) {
			throw new ValidationException(INVALID_PRICE);
		}
		if (certificate.getTags() != null) {
			validate(certificate.getTags());
		}
	}
	
	public void validate(List<Tag> tags) throws ValidationException {
		for (Tag tag : tags) {
			validateTagId(tag);
		}
	}
	
	private boolean isNotNull(Object object) {		
		return object != null; 
	}
	
	private boolean isCorrectName(String name) {
		return isNotNull(name) && name.matches(validationPropBundle.getString(REGEX_NAME));
	}
	
	private boolean isCorrectPassword(String password) {
		return password.matches(validationPropBundle.getString(REGEX_PASSWORD));
	}
	
	private boolean isCorrectDescription(String description) {	
		if (isNotNull(description)) {
			return description.matches(validationPropBundle.getString(REGEX_DESCRIPTION));
		}	
		return true;
	}
	
	private boolean isCorrectPrice(BigDecimal price) {
		return (isNotNull(price)) 
				&& ((price.compareTo(BigDecimal.valueOf(
						Integer.parseInt(validationPropBundle.getString(MAXIMUM_PRICE)))) == -1) 
				&& (price.compareTo(BigDecimal.valueOf(
						Integer.parseInt(validationPropBundle.getString(MINIMUM_PRICE)))) == 1))
				|| (price.compareTo(BigDecimal.ZERO) == 0);
	}
	
	private boolean isCorrectDuration(int duration) {		
		return (duration >= 0) && (duration <= Integer.parseInt(validationPropBundle
				.getString(MAXIMUM_DURATION)));
	}
}
