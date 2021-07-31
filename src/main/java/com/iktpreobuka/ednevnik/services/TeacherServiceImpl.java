package com.iktpreobuka.ednevnik.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.ednevnik.entities.TeacherEntity;
import com.iktpreobuka.ednevnik.repository.TeacherRepository;


@Service
public class TeacherServiceImpl implements TeacherService{
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Override
	public Boolean isActive(Integer teacherId) {
		if(teacherRepository.existsById(teacherId)) {
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			
			return true;
		}
		return true;
	}

}
