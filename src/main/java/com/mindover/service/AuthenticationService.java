package com.mindover.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mindover.dto.request.SignupUserRequest;
import com.mindover.entity.UserEntity;
import com.mindover.mapper.user.SignupUserRequestMapper;
import com.mindover.repository.AuthenticationRepository;

@Service
public class AuthenticationService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationRepository authRepository;
	
	@Autowired
	private SignupUserRequestMapper userMapper;

	/**
	 * Create a new user in data base
	 * 
	 * @param user
	 */
	public void registerNewUser(SignupUserRequest user) {

		UserEntity newUser = userMapper.signupUserToUserEntity(user);
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));

		authRepository.save(newUser);

	}

}
