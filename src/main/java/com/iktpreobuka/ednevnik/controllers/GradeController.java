package com.iktpreobuka.ednevnik.controllers;


import java.util.List;
import java.util.stream.Collectors;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
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
import com.iktpreobuka.ednevnik.entities.GradeEntity;
import com.iktpreobuka.ednevnik.entities.StudentTeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.dto.GradeDTO;
import com.iktpreobuka.ednevnik.models.EmailObject;
import com.iktpreobuka.ednevnik.repository.GradeRepository;
import com.iktpreobuka.ednevnik.repository.StudentTeacherCourseRepository;
import com.iktpreobuka.ednevnik.security.Viewes;
import com.iktpreobuka.ednevnik.services.EmailService;
import com.iktpreobuka.ednevnik.services.GradeService;
import com.iktpreobuka.ednevnik.services.StudentTeacherCourseService;
import com.iktpreobuka.ednevnik.utils.GradeCustomValidator;

@RestController
@RequestMapping(path = "/api/v1/project")
public class GradeController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Autowired
	public EmailService emailService;
	
	@Autowired
	public JavaMailSender emailSender;
	@Autowired
	private StudentTeacherCourseService studentTeacherCourseService;
	
	@Autowired
	private GradeCustomValidator gradeCustomValidator;
	
	@Autowired
	private GradeService gradeService;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(gradeCustomValidator);
	}

	// Dodaj novu ocenu
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	//@JsonView(Viewes.TeacherView.class)
	@PostMapping(value = "/grades")
	public ResponseEntity<?> createNew(@Valid @RequestBody GradeDTO newGrade, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			gradeCustomValidator.validate(newGrade, result);
		}
		GradeEntity grade = new GradeEntity();
		grade.setGrade(newGrade.getGrade());
		grade.setDate(newGrade.getDate());
		gradeRepository.save(grade);
		logger.info("Added grade: " + grade.getGrade() + " " + grade.getStudentTeacherCourse());
		return new ResponseEntity<GradeEntity>(grade, HttpStatus.CREATED);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Izmeni ocenu
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	//@JsonView(Viewes.TeacherView.class)
	@PutMapping(value = "/grade/{gradeId}")
	public GradeEntity updateGrade(@PathVariable Integer gradeId, @Valid @RequestBody GradeDTO newGrade) {
		GradeEntity grade = gradeRepository.findById(gradeId).get();
		if (newGrade.getGrade() != null) {
			grade.setGrade(newGrade.getGrade());
		}
		logger.info("Update grade: " + grade.getGrade());
		return gradeRepository.save(grade);
	}

	
	//dodaj ocenu i posalji mail
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	//@JsonView(Viewes.TeacherView.class)
	@PostMapping(value = "/{studentTeacherCourseId}/{gradeId}")
	public ResponseEntity<?> gradeStudent(@PathVariable Integer gradeId, @PathVariable Integer studentTeacherCourseId) {
		if (gradeRepository.existsById(gradeId) && gradeService.isActive(gradeId)) {
			if (studentTeacherCourseRepository.existsById(studentTeacherCourseId)
					&& studentTeacherCourseService.isActive(studentTeacherCourseId)) {
				GradeEntity grade = gradeRepository.findById(gradeId).get();
				StudentTeacherCourseEntity studentTeacherCourse = studentTeacherCourseRepository
						.findById(studentTeacherCourseId).get();
				grade.setStudentTeacherCourse(studentTeacherCourseRepository.findById(studentTeacherCourseId).get());

				EmailObject emailObject = new EmailObject();
				emailObject.setTo(studentTeacherCourse.getStudent().getParent().getEmail());
				emailObject.setSubject("Ucenik je dobio/la ocenu " + studentTeacherCourse.getStudent().getFirstName()
						+ " " + studentTeacherCourse.getStudent().getLastName() + " " + grade.getGrade());
				emailObject.setText("Vaše dete " + studentTeacherCourse.getStudent().getFirstName()+ " " + studentTeacherCourse.getStudent().getLastName() + " je dobilo ocenu " + grade.getGrade() + " kod profesora "
						+ studentTeacherCourse.getTeacherCourse().getTeacher().getFirstName() +  " "
						+ studentTeacherCourse.getTeacherCourse().getTeacher().getLastName());
						

				emailService.sendSimpleMessage(emailObject);
				logger.info("Ucenik je  ocenjen, roditelju poslat mejl.");

				studentTeacherCourseRepository.save(studentTeacherCourse);
				return new ResponseEntity<StudentTeacherCourseEntity>(studentTeacherCourse, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);

	}

	// Vrati po ID-u
	@Secured({"ROLE_ADMIN", "ROLE_STUDENT", "ROLE_TEACHER"})
	//@JsonView(Viewes.PrivateView.class)
	@GetMapping(value = "/grades/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (gradeRepository.existsById(id)) {
			return new ResponseEntity<GradeEntity>(gradeRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Grade not found."), HttpStatus.NOT_FOUND);
	}

	// Vrati sve ocene
	@Secured("ROLE_ADMIN")
	//@GetMapping(value = "/grades")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<GradeEntity>>(((List<GradeEntity>) gradeRepository.findAll()), HttpStatus.OK);
	}

}
