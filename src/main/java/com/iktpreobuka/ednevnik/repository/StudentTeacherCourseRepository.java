package com.iktpreobuka.ednevnik.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.ednevnik.entities.StudentEntity;
import com.iktpreobuka.ednevnik.entities.StudentTeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherCourseEntity;


public interface StudentTeacherCourseRepository extends CrudRepository<StudentTeacherCourseEntity, Integer> {

	Boolean existsByStudentAndTeacherCourse(StudentEntity student, TeacherCourseEntity teacherCourse);
	
	StudentTeacherCourseEntity findByStudentAndTeacherCourse(StudentEntity student, TeacherCourseEntity teacherCourse);
	
	List<StudentTeacherCourseEntity> findByStudent(StudentEntity student);

}
