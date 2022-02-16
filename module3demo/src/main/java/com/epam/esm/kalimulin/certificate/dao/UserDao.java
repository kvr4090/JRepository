package com.epam.esm.kalimulin.certificate.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;

public interface UserDao extends JpaRepository<UserClient, Integer> {
	
	List<UserClient> getAllUsers(PageModel model) throws DaoException;
	
	UserClient getUserById(UserClient user) throws DaoException;
	
	User getUserByName(User data) throws DaoException;
	
	User addUser(User user) throws DaoException;
}
