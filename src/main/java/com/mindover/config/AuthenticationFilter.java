package com.mindover.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindover.dto.request.SigninUserRequest;
import com.mindover.jwt.JwtUtil;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private JwtUtil jwtUtil;

	protected AuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authManager, JwtUtil jwtUtil) {
		super(defaultFilterProcessesUrl);
		setAuthenticationManager(authManager);
		this.jwtUtil = jwtUtil;

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		SigninUserRequest userData = new ObjectMapper().readValue(request.getInputStream(), SigninUserRequest.class);

		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		jwtUtil.addTokenToResponse(res, auth);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		Map<String, Object> data = new HashMap<>();
		data.put("message", failed.getMessage());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.addHeader("Authorization", "Messsage: " + failed.getMessage());

		response.getOutputStream().println(new ObjectMapper().writeValueAsString(data));
	}
	
}
