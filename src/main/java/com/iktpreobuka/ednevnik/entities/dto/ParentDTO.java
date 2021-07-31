package com.iktpreobuka.ednevnik.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.iktpreobuka.ednevnik.entities.RoleEntity;

public class ParentDTO {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	@NotBlank(message = "Confirm password must be provided.")
	@Size(min = 5, max = 100, message = "Confirm password must be between {min} and {max} characters long.")
	private String confirmPassword;

	private RoleEntity role;

	public ParentDTO() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

}
