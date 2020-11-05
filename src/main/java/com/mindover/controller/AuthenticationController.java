package com.mindover.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindover.dto.request.SignupUserRequest;
import com.mindover.service.AuthenticationService;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;

	@PostMapping(path = "/signup", consumes= MediaType.APPLICATION_JSON_VALUE)
	public final void signup(@RequestBody SignupUserRequest userRequest) {
		
		authService.registerNewUser(userRequest);
		
	}
	
}
