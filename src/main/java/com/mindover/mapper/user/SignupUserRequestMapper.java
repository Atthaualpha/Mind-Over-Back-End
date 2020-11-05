package com.mindover.mapper.user;

import org.mapstruct.Mapper;

import com.mindover.dto.request.SignupUserRequest;
import com.mindover.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface SignupUserRequestMapper {

	public UserEntity signupUserToUserEntity(SignupUserRequest dto); 
}
