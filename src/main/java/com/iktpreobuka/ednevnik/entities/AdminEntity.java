package com.iktpreobuka.ednevnik.entities;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "admins")
public class AdminEntity extends UserEntity {

	public AdminEntity() {
		super();

	}

}
