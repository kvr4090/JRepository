package com.epam.esm.kalimulin.certificate.service;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

public interface TagService {
	
	List<Tag> findAllTags(PageModel model) throws ServiceException, ValidationException;
	
	Tag addTag(Tag tag) throws ServiceException, ValidationException;
	
	String deleteTag(Tag tag) throws ServiceException, ValidationException;
	
	Tag findTag(Tag tag) throws ServiceException, ValidationException, EntityNotFoundException;
	
	Tag findMostPopularTagFromRichestUser() throws ServiceException;

}
