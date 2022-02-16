package com.epam.esm.kalimulin.certificate.service;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.controller.util.JwtResponse;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

public interface UserService {
	
	List<UserClient> getAllUsers(PageModel index) throws ServiceException, ValidationException;
	
	UserClient getUserById(UserClient user) throws ServiceException, ValidationException, EntityNotFoundException;
		
	User saveUser(User user) throws ServiceException, ValidationException;
	
	JwtResponse authUser(User user) throws ServiceException, ValidationException, EntityNotFoundException;
	
	boolean checkAccess(UserClient requiredUser) throws ServiceException, ValidationException;
}
