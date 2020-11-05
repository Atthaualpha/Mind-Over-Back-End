package com.mindover.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindover.dto.request.SigninUserRequest;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	protected AuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authManager) {
		super(defaultFilterProcessesUrl);
		setAuthenticationManager(authManager);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		SigninUserRequest userData = new ObjectMapper().readValue(request.getInputStream(), SigninUserRequest.class);

		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword()));
	}

}
