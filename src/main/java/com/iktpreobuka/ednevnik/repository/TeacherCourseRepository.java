package com.iktpreobuka.ednevnik.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.ednevnik.entities.CourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherEntity;


public interface TeacherCourseRepository extends CrudRepository<TeacherCourseEntity, Integer> {
	
	Boolean existsByTeacherAndCourse(TeacherEntity teacher, CourseEntity course);
	
	TeacherCourseEntity findByTeacherAndCourse(TeacherEntity teacher, CourseEntity course);
	
	List<TeacherCourseEntity> findByTeacher(TeacherEntity teacher);
	
	List<TeacherCourseEntity> findByCourse(CourseEntity course);

}
