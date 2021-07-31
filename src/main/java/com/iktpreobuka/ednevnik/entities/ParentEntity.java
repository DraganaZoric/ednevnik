package com.iktpreobuka.ednevnik.entities;


import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "parent")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ParentEntity extends UserEntity {

	@Column(nullable = false)
	@NotBlank(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String lastName;

	@JsonIgnore
	@OneToMany // (mappedBy = "parent", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	// @JsonBackReference
	private List<StudentEntity> students;

	public ParentEntity() {
		super();
	}

	public ParentEntity(Integer id,
			@NotBlank(message = "Username must be provided.") @Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters long.") String username,
			@NotBlank(message = "Password must be provided.") @Size(min = 3, max = 90, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Email must be provided.") @Email(message = "Email is not valid.") String email,
			RoleEntity role, Integer version,
			@NotBlank(message = "First name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String firstName,
			@NotBlank(message = "Last name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String lastName,
			List<StudentEntity> students) {
		super(id, username, password, email, role, version);
		this.firstName = firstName;
		this.lastName = lastName;
		this.students = students;
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

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

}
