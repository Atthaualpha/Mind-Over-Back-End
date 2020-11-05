package com.mindover.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SignupUserRequest {	
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
}
