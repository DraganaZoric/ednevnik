package com.iktpreobuka.ednevnik.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.ednevnik.entities.dto.GradeDTO;




@Component
public class GradeCustomValidator implements Validator{

	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return GradeDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GradeDTO grade = (GradeDTO) target;
		
		
		
	}

}
