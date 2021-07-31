package com.iktpreobuka.ednevnik.services;

import org.springframework.stereotype.Service;



@Service
public class GradeServiceImpl implements GradeService{

	

	@Override
	public Boolean isActive(Integer gradeId) {
		
		//if(gradeRepository.existsById(gradeId)) {
		//	GradeEntity grade = gradeRepository.findById(gradeId).get();
			return true;
		//}
		//return false;
	}

	
	
}
