package com.iktpreobuka.ednevnik.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.ednevnik.entities.ParentEntity;
import com.iktpreobuka.ednevnik.repository.ParentRepository;



@Service
public class ParentServiceImpl implements ParentService{
	
	@Autowired
	private ParentRepository parentRepository;
	
	@Override
	public Boolean isActive(Integer parentId) {
		if(parentRepository.existsById(parentId)) {
			ParentEntity parent = parentRepository.findById(parentId).get();
			//if(parent.getDeleted().equals(true)) {
		//		return false;
			//}
			return true;
		}
		return false;
	}

}
