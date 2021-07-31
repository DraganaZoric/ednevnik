package com.iktpreobuka.ednevnik.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.ednevnik.entities.TeacherCourseEntity;
import com.iktpreobuka.ednevnik.repository.TeacherCourseRepository;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService{
	
	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Override
	public Boolean isActive(Integer id) {
		if(teacherCourseRepository.existsById(id)) {
			TeacherCourseEntity teacherCourse = teacherCourseRepository.findById(id).get();
			//if(teacherCourse.getDeleted().equals(true) 
			//		|| teacherCourse.getCourse().getDeleted().equals(true)
			//		|| teacherCourse.getTeacher().getDeleted().equals(true)) {
			//	return false;
		//	}
			return true;
		}
		return false;
	}

}
