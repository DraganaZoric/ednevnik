package com.iktpreobuka.ednevnik.services;

import java.util.List;

import com.iktpreobuka.ednevnik.entities.StudentEntity;


public interface ClassService {

	List<StudentEntity> addCoursesForEntireClass(Integer classId, Integer courseId, Integer teacherId);
	
	Boolean isActive(Integer classId);

}
