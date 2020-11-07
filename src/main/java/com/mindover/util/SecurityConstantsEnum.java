package com.mindover.util;

import lombok.Getter;

@Getter
public enum SecurityConstantsEnum {

	AUTHORIZATION("Authorization"),
	AUTHORITIES("Authorities"),
	AUTHORIZATION_PREFIX("Bearer");	
	
	private String value;
	
	private SecurityConstantsEnum(String value) {
		this.value = value;
	}

}
