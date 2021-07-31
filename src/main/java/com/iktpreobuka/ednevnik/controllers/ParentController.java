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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.ednevnik.controllers.util.RESTError;
import com.iktpreobuka.ednevnik.entities.ParentEntity;

import com.iktpreobuka.ednevnik.entities.dto.ParentDTO;
import com.iktpreobuka.ednevnik.repository.ParentRepository;
import com.iktpreobuka.ednevnik.repository.RoleRepository;

import com.iktpreobuka.ednevnik.services.ParentService;
import com.iktpreobuka.ednevnik.utils.Encryption;
import com.iktpreobuka.ednevnik.utils.ParentCustomValidator;



@RestController
@RequestMapping(path = "/api/v1/project")
public class ParentController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ParentCustomValidator parentCustomValidator;

	@Autowired
	private ParentService parentService;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(parentCustomValidator);
	}

	// Dodaj novog roditelja
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/parents")
	public ResponseEntity<?> createNew(@Valid @RequestBody ParentDTO newParent, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			parentCustomValidator.validate(newParent, result);
		}
		ParentEntity parent = new ParentEntity();
		parent.setFirstName(newParent.getFirstName());
		parent.setLastName(newParent.getLastName());
		parent.setUsername(newParent.getUsername());
		parent.setEmail(newParent.getEmail());
		parent.setPassword(Encryption.getPassEncoded(newParent.getPassword()));
		parent.setRole(roleRepository.findById(4).get());
		parentRepository.save(parent);
		logger.info("Added parent: " + parent.getFirstName() + " " + parent.getLastName());
		return new ResponseEntity<ParentEntity>(parent, HttpStatus.CREATED);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Vrati sve roditelje
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/parents")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<ParentEntity>>(((List<ParentEntity>) parentRepository.findAll()), HttpStatus.OK);
	}

	// Vrati roditelja po ID-u
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/parents/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (parentRepository.existsById(id) && parentService.isActive(id)) {
			return new ResponseEntity<ParentEntity>(parentRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
	}

//		Izmeni roditelja
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/parents/{parentId}")
	public ResponseEntity<?> updateParent(@PathVariable Integer parentId, @Valid @RequestBody ParentDTO uparent,
			BindingResult result) {
		if (parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			ParentEntity parent = parentRepository.findById(parentId).get();
			parent.setFirstName(uparent.getFirstName());
			parent.setLastName(uparent.getLastName());
			parentRepository.save(parent);
			logger.info("Updated parent with ID: " + parent.getFirstName() + " " + parent.getLastName());
			return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Parent not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi roditelja
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/parents/{parentId}")
	public ResponseEntity<?> deleteParent(@PathVariable Integer parentId) {
		if (parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
			ParentEntity parent = parentRepository.findById(parentId).get();
			parentRepository.delete(parent);
			logger.info("Deleted parent with ID: " + parent.getFirstName() + " " + parent.getLastName());
			return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Parent not found."), HttpStatus.NOT_FOUND);
	}

}
