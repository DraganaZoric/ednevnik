package com.iktpreobuka.ednevnik.repository;

import org.springframework.data.repository.CrudRepository;


import com.iktpreobuka.ednevnik.entities.ParentEntity;


public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {
	
	
	Boolean existsByEmail(String email);
	

}
