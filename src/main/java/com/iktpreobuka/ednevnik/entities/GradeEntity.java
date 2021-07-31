package com.iktpreobuka.ednevnik.entities;

import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.ednevnik.security.Viewes;

@Entity
@Table(name = "grade")
public class GradeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@JsonView(Viewes.AdminView.class)
	private Integer id;

	@Column(name = "grade", nullable = false)
	@NotNull(message = "Grade must be provided.")
	@Min(value = 1, message = "Grade must be greater than {value}.")
	@Max(value = 5, message = "Grade must be lesser than than {value}.")
	@JsonView(Viewes.StudentView.class)
	private Integer grade;

	@Column(name = "date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Viewes.StudentView.class)
	protected LocalDate date;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "studentTeacherCourse")
	private StudentTeacherCourseEntity studentTeacherCourse;

	@Version
	private Integer version;

	public GradeEntity() {
		super();
	}

	public GradeEntity(Integer id,
			@NotNull(message = "Grade must be provided.") @Min(value = 1, message = "Grade must be greater than {value}.") @Max(value = 5, message = "Grade must be lesser than than {value}.") Integer grade,
			LocalDate date, StudentTeacherCourseEntity studentTeacherCourse, Integer version) {
		super();
		this.id = id;
		this.grade = grade;
		this.date = date;
		this.studentTeacherCourse = studentTeacherCourse;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public StudentTeacherCourseEntity getStudentTeacherCourse() {
		return studentTeacherCourse;
	}

	public void setStudentTeacherCourse(StudentTeacherCourseEntity studentTeacherCourse) {
		this.studentTeacherCourse = studentTeacherCourse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
