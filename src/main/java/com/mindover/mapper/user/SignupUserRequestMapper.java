package com.mindover.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mindover.dto.request.SignupUserRequest;
import com.mindover.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface SignupUserRequestMapper {
	
	@Mapping(target = "id", ignore = true)
	UserEntity signupUserToUserEntity(SignupUserRequest dto); 
}
