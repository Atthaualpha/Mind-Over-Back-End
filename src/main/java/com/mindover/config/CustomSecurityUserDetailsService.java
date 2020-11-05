package com.mindover.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mindover.projections.UserCredentials;
import com.mindover.repository.AuthenticationRepository;

public class CustomSecurityUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthenticationRepository authRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {

		UserCredentials user = authRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("Cannot find user with email " + email);
		}

		return new User(email, "{bcrypt}" + user.getPassword(), Collections.emptyList());
	}
}
