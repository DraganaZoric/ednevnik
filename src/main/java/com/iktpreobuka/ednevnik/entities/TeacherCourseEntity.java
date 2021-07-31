package com.iktpreobuka.ednevnik.entities;

import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "teacher_course")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherCourseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "course")
	private CourseEntity course;

	@ManyToOne // (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	// @JsonBackReference
	@JoinColumn(name = "teacher")
	private TeacherEntity teacher;

	@JsonIgnore
	@OneToMany // (mappedBy = "teacherCourse", cascade = CascadeType.REFRESH, fetch =
				// FetchType.LAZY)
//	@JsonBackReference
	// @JsonManagedReference
	private List<StudentTeacherCourseEntity> studentTeacherCourse;

	@Version
	private Integer version;

	public TeacherCourseEntity(Integer id, CourseEntity course, TeacherEntity teacher,
			List<StudentTeacherCourseEntity> studentTeacherCourse, Integer version) {
		super();
		this.id = id;
		this.course = course;
		this.teacher = teacher;
		this.studentTeacherCourse = studentTeacherCourse;
		this.version = version;
	}

	public TeacherCourseEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CourseEntity getCourse() {
		return course;
	}

	public void setCourse(CourseEntity course) {
		this.course = course;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public List<StudentTeacherCourseEntity> getStudentTeacherCourse() {
		return studentTeacherCourse;
	}

	public void setStudentTeacherCourse(List<StudentTeacherCourseEntity> studentTeacherCourse) {
		this.studentTeacherCourse = studentTeacherCourse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}