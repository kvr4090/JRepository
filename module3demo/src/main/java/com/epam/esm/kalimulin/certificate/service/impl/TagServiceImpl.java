package com.epam.esm.kalimulin.certificate.service.impl;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_ID;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.DELETED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.dao.TagDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.service.TagService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;
import com.epam.esm.kalimulin.certificate.service.util.AppValidator;

@Component
public class TagServiceImpl implements TagService {
	
	private final TagDao tagDao;														
	private final AppValidator validator;
	
	@Autowired
	public TagServiceImpl(TagDao tagDao, AppValidator validator) {
		this.tagDao = tagDao;
		this.validator = validator;
	}

	@Override
	public Tag addTag(Tag tag) throws ServiceException, ValidationException {
		validator.validate(tag);
		try {
			return tagDao.addTag(tag);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public String deleteTag(Tag tag) throws ServiceException, ValidationException {
		validator.validate(tag.getId());
		try {
			if (tagDao.findTagById(tag) == null) {
				throw new ValidationException(INVALID_ID);
			}
			tagDao.deleteTag(tag);
			return DELETED;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Tag findTag(Tag tag) throws ServiceException, ValidationException, EntityNotFoundException {
		validator.validate(tag.getId());
		try {
			if (tag.getId() != 0) {
				tag = tagDao.findTagById(tag);
			} else {
				tag = tagDao.findTagByName(tag);
			}
			if (tag == null) {
				throw new EntityNotFoundException();
			}
			return tag;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Tag> findAllTags(PageModel model) throws ServiceException {
		try {
			return tagDao.findAllTags(model);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}
	}

	@Override
	public Tag findMostPopularTagFromRichestUser() throws ServiceException {
		try {
			return tagDao.findMostPopularTagFromRichestUser();
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}
	}
}
