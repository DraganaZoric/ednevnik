package com.iktpreobuka.ednevnik.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.ednevnik.entities.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUsername(String username);
	
	
}
