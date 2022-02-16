package com.epam.esm.kalimulin.certificate.dao;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

public interface TagDao {
	
	List<Tag> findAllTags(PageModel model) throws DaoException;
	
	Tag addTag(Tag tag) throws DaoException;
	
	void deleteTag(Tag tag) throws DaoException;
	
	Tag findTagByName(Tag tag) throws DaoException;
	
	Tag findTagById(Tag tag) throws DaoException;
	
	Tag findMostPopularTagFromRichestUser() throws DaoException;

}
