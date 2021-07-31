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

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.ednevnik.controllers.util.RESTError;
import com.iktpreobuka.ednevnik.entities.CourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherEntity;
import com.iktpreobuka.ednevnik.entities.dto.TeacherDTO;

import com.iktpreobuka.ednevnik.repository.CourseRepository;
import com.iktpreobuka.ednevnik.repository.RoleRepository;
import com.iktpreobuka.ednevnik.repository.TeacherCourseRepository;
import com.iktpreobuka.ednevnik.repository.TeacherRepository;
import com.iktpreobuka.ednevnik.security.Viewes;
import com.iktpreobuka.ednevnik.services.CourseService;
import com.iktpreobuka.ednevnik.services.TeacherService;
import com.iktpreobuka.ednevnik.utils.Encryption;
import com.iktpreobuka.ednevnik.utils.TeacherCustomValidator;

@RestController
@RequestMapping(path = "/api/v1/project")
public class TeacherController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;

	@Autowired
	private TeacherCustomValidator teacherCustomValidator;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private CourseService courseService;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(teacherCustomValidator);
	}

	// Dodaj novog profesora
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PostMapping(value = "/teachers")
	public ResponseEntity<?> createNew(@Valid @RequestBody TeacherDTO newTeacher, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			teacherCustomValidator.validate(newTeacher, result);
		}
		TeacherEntity teacher = new TeacherEntity();
		teacher.setFirstName(newTeacher.getFirstName());
		teacher.setLastName(newTeacher.getLastName());
		teacher.setUsername(newTeacher.getUsername());
		teacher.setEmail(newTeacher.getEmail());
		teacher.setPassword(Encryption.getPassEncoded(newTeacher.getPassword()));
		teacher.setRole(roleRepository.findById(2).get());
		teacherRepository.save(teacher);
		logger.info("Added teacher: " + teacher.getFirstName() + " " + teacher.getLastName());
		return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.CREATED);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Vrati sve profesore
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@GetMapping(value = "/teachers")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<TeacherEntity>>(((List<TeacherEntity>) teacherRepository.findAll()),
				HttpStatus.CREATED);
	}

	// vraća profesora po vrednosti prosleđenog ID-a
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	//@JsonView(Viewes.TeacherView.class)
	@GetMapping(value = "/teachers/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (teacherRepository.existsById(id) && teacherService.isActive(id)) {
			return new ResponseEntity<TeacherEntity>(teacherRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);

	}

//	Izmeni profesora
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PutMapping(value = "/teachers/{teacherId}")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer teacherId, @Valid @RequestBody TeacherDTO uteacher,
			BindingResult result) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			teacher.setFirstName(uteacher.getFirstName());
			teacher.setLastName(uteacher.getLastName());
			teacherRepository.save(teacher);
			logger.info("Updated teacher with ID: " + teacher.getFirstName() + " " + teacher.getLastName());
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.CREATED);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi profesora
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@DeleteMapping(value = "/teachers/{teacherId}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Integer teacherId) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			teacherRepository.delete(teacher);
			logger.info("Deleted teacher with ID: " + teacher.getFirstName() + " " + teacher.getLastName());
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj predmet za profesora
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PostMapping(value = "/{teacherId}/courses/{courseId}")
	public ResponseEntity<?> addCourseForTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
				TeacherEntity teacher = teacherRepository.findById(teacherId).get();
				CourseEntity course = courseRepository.findById(courseId).get();
				if (!teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
					TeacherCourseEntity TCE = new TeacherCourseEntity();
					TCE.setTeacher(teacherRepository.findById(teacherId).get());
					TCE.setCourse(courseRepository.findById(courseId).get());
					teacherCourseRepository.save(TCE);
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(),
							HttpStatus.OK);
				}
				TeacherCourseEntity TCE = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
				teacherCourseRepository.save(TCE);
				return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);

	}

}