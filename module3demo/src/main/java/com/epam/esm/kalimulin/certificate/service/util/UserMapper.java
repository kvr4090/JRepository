package com.epam.esm.kalimulin.certificate.service.util;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.ROLE;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.epam.esm.kalimulin.certificate.bean.user.User;
import com.epam.esm.kalimulin.certificate.bean.user.UserPrincipal;

import java.util.stream.Collectors;

public class UserMapper {
	
	public static UserPrincipal userToPrincipal(User user) {
		UserPrincipal userPrincipal = new UserPrincipal();
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(ROLE + role.getName())).collect(Collectors.toList());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setEnabled(user.isEnabled());
        userPrincipal.setAuthorities(authorities);
        return userPrincipal;
	}
}
