package com.epam.esm.kalimulin.certificate.service.impl;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.USER_NOT_FOUND;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_NAME;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.INVALID_CREDENTIALS;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.ROLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.user.Role;
import com.epam.esm.kalimulin.certificate.bean.user.RoleName;
import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.controller.util.JwtProvider;
import com.epam.esm.kalimulin.certificate.controller.util.JwtResponse;
import com.epam.esm.kalimulin.certificate.dao.UserDao;
import com.epam.esm.kalimulin.certificate.dao.exception.DaoException;
import com.epam.esm.kalimulin.certificate.service.UserService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;
import com.epam.esm.kalimulin.certificate.service.util.AppValidator;
import com.epam.esm.kalimulin.certificate.service.util.UserMapper;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final UserDao userDao;
	private final AppValidator validator;
	private final AuthenticationManager authenticationManager;
    private final JwtProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserDao userDao, AppValidator validator, AuthenticationManager authenticationManager,
			JwtProvider tokenProvider, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.validator = validator;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserClient> getAllUsers(PageModel model) throws ServiceException, ValidationException {
		validator.validate(model);		
		try {
			return userDao.getAllUsers(model);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public UserClient getUserById(UserClient user) throws ServiceException, ValidationException, EntityNotFoundException {
		validator.validate(user.getId());
		try {
			user = userDao.getUserById(user);
			if (user == null) {
				throw new EntityNotFoundException();
			}
			return user;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User();
		user.setUsername(username);
		try {
			return UserMapper.userToPrincipal(userDao.getUserByName(user));
		} catch (DaoException e) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
	}
	
	@Transactional
	@Override
	public User saveUser(User user) throws ServiceException, ValidationException {
		validator.validate(user);
		try {
			Role role = findRole(RoleName.USER);
			List<Role> userRole = new ArrayList<>();
			userRole.add(role);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
    		user.setRoles(userRole);
    		user.setEnabled(true);
    		if (checkSameUsername(user)) {
    			return userDao.addUser(user);
			} else {
				throw new ValidationException(INVALID_NAME);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public JwtResponse authUser(User user) throws ServiceException, ValidationException, EntityNotFoundException {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		try {
			Authentication authentication = authenticationManager.authenticate(authToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = tokenProvider.generateToken(authentication);
			return new JwtResponse(jwt);		
		} catch (InternalAuthenticationServiceException | BadCredentialsException e) {
			throw new EntityNotFoundException(INVALID_CREDENTIALS);
		}
	}

	private Role findRole(RoleName name) throws ServiceException {
		Role role = new Role();
		role.setName(RoleName.USER);
		role.setId(2);
		return role;
	}
	
	private boolean checkSameUsername(User user) throws DaoException {
		return Objects.isNull(userDao.getUserByName(user));
	}

	@Override
	public boolean checkAccess(UserClient requiredUser) throws ServiceException, ValidationException {
		validator.validate(requiredUser.getId());
		try {
			User user = getCurrentUser();
			user = userDao.getUserByName(user);
			Optional<Role> adminRole = user.getRoles()
					.stream()
					.filter(p -> p.getName().equals(RoleName.ADMINISTRATOR))
					.findFirst();			
			return (adminRole.isPresent()) || (user.getId() == requiredUser.getId());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private User getCurrentUser() {
		User user = new User();
		List<Role> listRole = new ArrayList<>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			user.setUsername(((UserDetails)principal).getUsername());
		} else {
			user.setUsername(principal.toString());
		}
		List<SimpleGrantedAuthority> list = (List<SimpleGrantedAuthority>) ((UserDetails)principal).getAuthorities();
		for (SimpleGrantedAuthority authority : list) {
			Role role = new Role();
			role.setName(RoleName.valueOf(authority.getAuthority().replace(ROLE, "")));
			listRole.add(role);
		}
		user.setRoles(listRole);
		return user;
	}
}