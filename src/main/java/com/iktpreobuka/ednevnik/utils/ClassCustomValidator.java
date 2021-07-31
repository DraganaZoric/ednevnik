package com.iktpreobuka.ednevnik.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.ednevnik.entities.dto.ClassDTO;
import com.iktpreobuka.ednevnik.repository.ClassRepository;

@Component
public class ClassCustomValidator implements Validator{

	
	@Autowired
	private ClassRepository classRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return ClassDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClassDTO classEntity = (ClassDTO) target;
		
	}
		
	}
	
	
	

