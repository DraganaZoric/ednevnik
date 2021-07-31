package com.iktpreobuka.ednevnik.entities;

import java.util.List;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.ednevnik.security.Viewes;

@Entity
@Table(name = "student_teacher_course")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class StudentTeacherCourseEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student")
	private StudentEntity student;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacherCourse")
	private TeacherCourseEntity teacherCourse;

	@JsonIgnore
	@JsonView(Viewes.TeacherView.class)
	@OneToMany // (mappedBy = "studentTeacherCourse", cascade = CascadeType.REFRESH, fetch =
				// FetchType.LAZY)
	// @JsonBackReference
	private List<GradeEntity> grades;

	@Version
	private Integer version;

	public StudentTeacherCourseEntity() {
		super();
	}

	public StudentTeacherCourseEntity(Integer id, StudentEntity student, TeacherCourseEntity teacherCourse,
			List<GradeEntity> grades, Integer version) {
		super();
		this.id = id;
		this.student = student;
		this.teacherCourse = teacherCourse;
		this.grades = grades;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public TeacherCourseEntity getTeacherCourse() {
		return teacherCourse;
	}

	public void setTeacherCourse(TeacherCourseEntity teacherCourse) {
		this.teacherCourse = teacherCourse;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
