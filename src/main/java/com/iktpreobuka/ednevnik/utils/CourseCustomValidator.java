package com.iktpreobuka.ednevnik.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.ednevnik.entities.dto.CourseDTO;
import com.iktpreobuka.ednevnik.repository.CourseRepository;



@Component
public class CourseCustomValidator implements Validator{
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CourseDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CourseDTO course = (CourseDTO) target;
		
		
	}
		
	}

	
	
	
