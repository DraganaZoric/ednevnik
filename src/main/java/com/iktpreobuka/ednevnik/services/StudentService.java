package com.iktpreobuka.ednevnik.services;



import com.iktpreobuka.ednevnik.entities.StudentEntity;



public interface StudentService {
	
	StudentEntity addCourseForStudent(Integer studentId, Integer courseId, Integer teacherId);
	
	Boolean isActive(Integer studentId);
	
	//List<TeacherCourseEntity> getCourses(Integer studentId);

}
