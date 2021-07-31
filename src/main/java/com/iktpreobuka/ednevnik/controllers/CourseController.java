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

import com.iktpreobuka.ednevnik.entities.dto.CourseDTO;

import com.iktpreobuka.ednevnik.repository.CourseRepository;
import com.iktpreobuka.ednevnik.security.Viewes;
import com.iktpreobuka.ednevnik.services.CourseService;
import com.iktpreobuka.ednevnik.utils.CourseCustomValidator;


@RestController
@RequestMapping(path = "/api/v1/project")
public class CourseController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseCustomValidator courseCustomValidator;

	@Autowired
	private CourseService courseService;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(courseCustomValidator);
	}

	// Dodaj predmet
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/course")
	public ResponseEntity<?> createNew(@Valid @RequestBody CourseDTO newCourse, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			courseCustomValidator.validate(newCourse, result);
		}
		CourseEntity course = new CourseEntity();
		course.setName(newCourse.getName());
		course.setSemester(newCourse.getSemester());
		course.setWeeklyHours(newCourse.getWeeklyHours());
		courseRepository.save(course);
		logger.info("Added course: " + course.getName());
		return new ResponseEntity<CourseEntity>(course, HttpStatus.CREATED);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Vrati sve predmete
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@GetMapping(value = "/course")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<CourseEntity>>(((List<CourseEntity>) courseRepository.findAll()), HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	//@JsonView(Viewes.TeacherView.class)
	@GetMapping(value = "/courses/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (courseRepository.existsById(id) && courseService.isActive(id)) {
			return new ResponseEntity<CourseEntity>(courseRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	// izmeni predmet
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{courseId}")
	public ResponseEntity<?> updateCourse(@PathVariable Integer courseId, @Valid @RequestBody CourseDTO ucourse,
			BindingResult result) {
		if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			CourseEntity course = courseRepository.findById(courseId).get();
			course.setSemester(ucourse.getSemester());
			course.setWeeklyHours(ucourse.getWeeklyHours());
			courseRepository.save(course);
			logger.info("Updated course with name: " + course.getName());
			return new ResponseEntity<CourseEntity>(course, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi po ID-u
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/course/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		if (courseRepository.existsById(id) && courseService.isActive(id)) {
			CourseEntity course = courseRepository.findById(id).get();
			courseRepository.save(course);
			logger.info("Deleted course : " + course.getName());
			return new ResponseEntity<CourseEntity>(course, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

}
