package com.iktpreobuka.ednevnik.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "student")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class StudentEntity extends UserEntity {

	@Column(nullable = false)
	@NotBlank(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String lastName;

	@Column(nullable = false, unique = true)
	@NotNull(message = "Personal ID number must be provided.")
	@Pattern(regexp = "^\\d{13}$", message = "Personal ID number must be exactly 13 characters long.")
	private String jmbg;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "attendingClass")
	private ClassEntity attendingClass;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private ParentEntity parent;

	@JsonIgnore
	@OneToMany // (mappedBy = "student", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	// @JsonIgnore@JsonBackReference
	private List<StudentTeacherCourseEntity> studentTeacherCourse;

	public StudentEntity() {
		super();
	}

	public StudentEntity(Integer id,
			@NotBlank(message = "Username must be provided.") @Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters long.") String username,
			@NotBlank(message = "Password must be provided.") @Size(min = 3, max = 90, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Email must be provided.") @Email(message = "Email is not valid.") String email,
			RoleEntity role, Integer version,
			@NotBlank(message = "First name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String firstName,
			@NotBlank(message = "Last name must be provided.") @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.") String lastName,
			@NotNull(message = "Personal ID number must be provided.") @Pattern(regexp = "^\\d{13}$", message = "Personal ID number must be exactly 13 characters long.") String jmbg,
			ClassEntity attendingClass, ParentEntity parent, List<StudentTeacherCourseEntity> studentTeacherCourse) {
		super(id, username, password, email, role, version);
		this.firstName = firstName;
		this.lastName = lastName;
		this.jmbg = jmbg;
		this.attendingClass = attendingClass;
		this.parent = parent;
		this.studentTeacherCourse = studentTeacherCourse;
	}

	public ParentEntity getParent() {
		return parent;
	}

	public void setParent(ParentEntity parent) {
		this.parent = parent;
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

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public ClassEntity getAttendingClass() {
		return attendingClass;
	}

	public void setAttendingClass(ClassEntity attendingClass) {
		this.attendingClass = attendingClass;
	}

	public List<StudentTeacherCourseEntity> getStudentTeacherCourse() {
		return studentTeacherCourse;
	}

	public void setStudentTeacherCourse(List<StudentTeacherCourseEntity> studentTeacherCourse) {
		this.studentTeacherCourse = studentTeacherCourse;
	}	
	

}
