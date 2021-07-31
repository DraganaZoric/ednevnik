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
import com.iktpreobuka.ednevnik.entities.StudentEntity;
import com.iktpreobuka.ednevnik.entities.TeacherEntity;
import com.iktpreobuka.ednevnik.entities.dto.StudentDTO;

import com.iktpreobuka.ednevnik.repository.ClassRepository;
import com.iktpreobuka.ednevnik.repository.CourseRepository;
import com.iktpreobuka.ednevnik.repository.ParentRepository;
import com.iktpreobuka.ednevnik.repository.RoleRepository;
import com.iktpreobuka.ednevnik.repository.StudentRepository;
import com.iktpreobuka.ednevnik.repository.TeacherCourseRepository;
import com.iktpreobuka.ednevnik.repository.TeacherRepository;
import com.iktpreobuka.ednevnik.security.Viewes;
import com.iktpreobuka.ednevnik.services.ClassService;
import com.iktpreobuka.ednevnik.services.CourseService;
import com.iktpreobuka.ednevnik.services.ParentService;
import com.iktpreobuka.ednevnik.services.StudentService;
import com.iktpreobuka.ednevnik.services.TeacherCourseService;
import com.iktpreobuka.ednevnik.services.TeacherService;
import com.iktpreobuka.ednevnik.utils.Encryption;

import com.iktpreobuka.ednevnik.utils.StudentCustomValidator;

@RestController
@RequestMapping(path = "/api/v1/project")
public class StudentController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;

	@Autowired
	private StudentCustomValidator studentCustomValidator;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ParentService parentService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private ClassService classService;

	@Autowired
	private TeacherCourseService teacherCourseService;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(studentCustomValidator);
	}

	// Dodaj novog ucenika
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PostMapping(value = "/students")
	public ResponseEntity<?> createNew(@Valid @RequestBody StudentDTO newStudent, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			studentCustomValidator.validate(newStudent, result);
		}
		StudentEntity student = new StudentEntity();
		student.setFirstName(newStudent.getFirstName());
		student.setLastName(newStudent.getLastName());
		student.setUsername(newStudent.getUsername());
		student.setJmbg(newStudent.getJmbg());
		student.setEmail(newStudent.getEmail());
		student.setPassword(Encryption.getPassEncoded(newStudent.getPassword()));
		student.setRole(roleRepository.findById(3).get());
		studentRepository.save(student);
		logger.info("Added student: " + student.getFirstName() + " " + student.getLastName());
		return new ResponseEntity<StudentEntity>(student, HttpStatus.CREATED);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));
	}

	// Vrati sve ucenike
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@GetMapping(value = "/students")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<StudentEntity>>(((List<StudentEntity>) studentRepository.findAll()),
				HttpStatus.OK);
	}

	// vraća ucenika po vrednosti prosleđenog ID-a
		@Secured({"ROLE_ADMIN", "ROLE_PARENT"})
		//@JsonView(Viewes.ParentView.class)
		@GetMapping(value = "/students/{id}")
		public ResponseEntity<?> getById(@PathVariable Integer id) {
			if (studentRepository.existsById(id) && studentService.isActive(id)) {
				return new ResponseEntity<StudentEntity>(studentRepository.findById(id).get(), HttpStatus.OK);
			}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
			}

//		Izmeni ucenika
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PutMapping(value = "/students/{studentId}")
	public ResponseEntity<?> updateStudent(@PathVariable Integer studentId, @Valid @RequestBody StudentDTO ustudent,
			BindingResult result) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			StudentEntity student = studentRepository.findById(studentId).get();
			student.setFirstName(ustudent.getFirstName());
			student.setLastName(ustudent.getLastName());
			studentRepository.save(student);
			logger.info("Updated student with ID: " + student.getFirstName() + " " + student.getLastName());
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi ucenika
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@DeleteMapping(value = "/students/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			StudentEntity student = studentRepository.findById(studentId).get();
			// student.setDeleted(true);
			studentRepository.delete(student);
			logger.info("Deleted student with ID: " + student.getFirstName() + " " + student.getLastName());
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj roditelja uceniku
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.AdminView.class)
	@PostMapping(value = "/{studentId}/parent/{parentId}")
	public ResponseEntity<?> addParent(@PathVariable Integer studentId, @PathVariable Integer parentId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				student.setParent(parentRepository.findById(parentId).get());
				studentRepository.save(student);
				logger.info("Add parent" + parentId + " for teacherstudent " + student.getFirstName() + " " + student.getLastName());
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj predmet uceniku
	@Secured("ROLE_ADMIN")
	//@JsonView(Viewes.TeacherView.class)
	@PostMapping(value = "/{studentId}/courses/{courseId}/teachers/{teacherId}")
	public ResponseEntity<?> addCourseForStudent(@PathVariable Integer studentId, @PathVariable Integer courseId,
			@PathVariable Integer teacherId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
				if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
					TeacherEntity teacher = teacherRepository.findById(teacherId).get();
					CourseEntity course = courseRepository.findById(courseId).get();
					if (teacherCourseRepository.existsByTeacherAndCourse(teacher, course) && teacherCourseService
							.isActive(teacherCourseRepository.findByTeacherAndCourse(teacher, course).getId())) {
						StudentEntity student = studentService.addCourseForStudent(studentId, courseId, teacherId);
						if (student != null) {
							return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
						}
						return new ResponseEntity<RESTError>(new RESTError(10, "Student already has that course."),
								HttpStatus.NOT_FOUND);
					}
					return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
							HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj odeljenje za ucenika
	@Secured("ROLE_ADMIN")
	@JsonView(Viewes.TeacherView.class)
	@PostMapping(value = "/{studentId}/class/{classId}")
	public ResponseEntity<?> addClass(@PathVariable Integer studentId, @PathVariable Integer classId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (classRepository.existsById(classId) && classService.isActive(classId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				student.setAttendingClass(classRepository.findById(classId).get());
				studentRepository.save(student);
				
				logger.info("Add class" + classId + " for student " + student.getFirstName() + " " + student.getLastName());
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}
}
