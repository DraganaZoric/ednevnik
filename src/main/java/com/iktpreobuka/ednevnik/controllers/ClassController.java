package com.iktpreobuka.ednevnik.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.ednevnik.controllers.util.RESTError;
import com.iktpreobuka.ednevnik.entities.ClassEntity;
import com.iktpreobuka.ednevnik.entities.dto.ClassDTO;
import com.iktpreobuka.ednevnik.repository.ClassRepository;
import com.iktpreobuka.ednevnik.services.ClassService;
import com.iktpreobuka.ednevnik.utils.ClassCustomValidator;


@RestController
@RequestMapping(path = "/api/v1/project")
public class ClassController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private ClassCustomValidator classCustomValidator;

	@Autowired
	private ClassService classService;

	// Dodaj novi razred
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/class")
	public ResponseEntity<?> createNew(@Valid @RequestBody ClassDTO newClass, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			classCustomValidator.validate(newClass, result);
		}
		ClassEntity classEntity = new ClassEntity();
		classEntity.setClassNumber(newClass.getClassNumber());
		classEntity.setSectionNo(newClass.getSectionNo());
		classEntity.setGeneration(newClass.getGeneration());
		classRepository.save(classEntity);
		logger.info("Added new class: " + classEntity.getClassNumber());
		return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Vrati sve razrede
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/class")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<ClassEntity>>(((List<ClassEntity>) classRepository.findAll()),
				HttpStatus.CREATED);
	}

	// izmeni odeljenje
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/class/{classId}")
	public ResponseEntity<?> updateClass(@PathVariable Integer classId, @Valid @RequestBody ClassDTO uClass,
			BindingResult result) {
		if (classRepository.existsById(classId) && classService.isActive(classId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			ClassEntity clazz = classRepository.findById(classId).get();
			clazz.setSectionNo(uClass.getSectionNo());
			classRepository.save(clazz);
			logger.info("Updated class with ID:" + classId.toString());
			return new ResponseEntity<ClassEntity>(clazz, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi razred po ID-u
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		if (classRepository.existsById(id) && classService.isActive(id)) {
			ClassEntity clazz = classRepository.findById(id).get();
			classRepository.save(clazz);
			logger.info("Deleted class with ID: " + id.toString());
			return new ResponseEntity<ClassEntity>(clazz, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
	}

}

