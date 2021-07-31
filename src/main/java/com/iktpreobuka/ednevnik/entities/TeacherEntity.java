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

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.ednevnik.security.Viewes;


@Entity
@Table(name = "teacher")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherEntity extends UserEntity {

	@Column(nullable = false)
	@NotBlank(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String lastName;

	@JsonIgnore
	@JsonView(Viewes.TeacherView.class)
	@OneToMany // (mappedBy = "teacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	// @JsonBackReference
	// @JsonManagedReference
	private List<TeacherCourseEntity> teacherCourse;

	public TeacherEntity() {
		super();
	}

	public TeacherEntity(Integer id,
			@NotBlank(message = "Username must be provided.") @Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters long.") String username,
			@NotBlank(message = "Password must be provided.") @Size(min = 3, max = 90, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Email must be provided.") @Email(message = "Email is not valid.") String email,
			RoleEntity role, Integer version,
			@NotBlank(message = "First name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String firstName,
			@NotBlank(message = "Last name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String lastName,
			List<TeacherCourseEntity> teacherCourse) {
		super(id, username, password, email, role, version);
		this.firstName = firstName;
		this.lastName = lastName;
		this.teacherCourse = teacherCourse;
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

	public List<TeacherCourseEntity> getTeacherCourse() {
		return teacherCourse;
	}

	public void setTeacherCourse(List<TeacherCourseEntity> teacherCourse) {
		this.teacherCourse = teacherCourse;
	}

}
