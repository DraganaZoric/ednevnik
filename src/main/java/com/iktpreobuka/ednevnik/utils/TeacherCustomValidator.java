package com.iktpreobuka.ednevnik.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.ednevnik.entities.dto.TeacherDTO;


@Component
public class TeacherCustomValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return TeacherDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TeacherDTO teacher = (TeacherDTO) target;
		
		if(!teacher.getPassword().equals(teacher.getConfirmPassword())) {
		errors.reject("400", "Passwords don't match.");
		}
		
	}

}
