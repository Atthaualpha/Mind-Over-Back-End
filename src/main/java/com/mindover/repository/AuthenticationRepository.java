package com.mindover.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mindover.entity.UserEntity;
import com.mindover.projections.UserCredentials;

@Repository
public interface AuthenticationRepository extends CrudRepository<UserEntity, Long>{

	UserCredentials findByEmail(String email);	
}
