package com.iktpreobuka.ednevnik.repository;

import org.springframework.data.repository.CrudRepository;
import com.iktpreobuka.ednevnik.entities.RoleEntity;


public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
	
	//RoleEntity findByName(String name);
	
}
