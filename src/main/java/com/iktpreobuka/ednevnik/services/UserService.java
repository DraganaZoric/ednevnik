package com.iktpreobuka.ednevnik.services;

import java.util.Optional;

import com.iktpreobuka.ednevnik.entities.RoleEntity;



public interface UserService {

	public boolean isAuthorizedAs(RoleEntity role);
	public Optional<String> getLoggedInUsername();
}
