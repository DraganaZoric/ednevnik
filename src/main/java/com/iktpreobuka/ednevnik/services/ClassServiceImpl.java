package com.iktpreobuka.ednevnik.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.ednevnik.entities.ClassEntity;
import com.iktpreobuka.ednevnik.entities.CourseEntity;
import com.iktpreobuka.ednevnik.entities.StudentEntity;
import com.iktpreobuka.ednevnik.entities.StudentTeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherCourseEntity;
import com.iktpreobuka.ednevnik.entities.TeacherEntity;
import com.iktpreobuka.ednevnik.repository.ClassRepository;
import com.iktpreobuka.ednevnik.repository.CourseRepository;
import com.iktpreobuka.ednevnik.repository.StudentRepository;
import com.iktpreobuka.ednevnik.repository.StudentTeacherCourseRepository;
import com.iktpreobuka.ednevnik.repository.TeacherCourseRepository;
import com.iktpreobuka.ednevnik.repository.TeacherRepository;



@Service
public class ClassServiceImpl implements ClassService {

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;

	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;

	@Override
	public List<StudentEntity> addCoursesForEntireClass(Integer classId, Integer courseId, Integer teacherId) {
		ClassEntity classEntity = classRepository.findById(classId).get();
		CourseEntity course = courseRepository.findById(courseId).get();
		TeacherEntity teacher = teacherRepository.findById(teacherId).get();
		TeacherCourseEntity teacherCourse = teacherCourseRepository.findByTeacherAndCourse(teacher, course);

		List<StudentEntity> studentsOfClass = studentRepository.findByAttendingClass(classEntity);
				//.stream().filter(student -> !student.getDeleted().equals(true)).collect(Collectors.toList());
		List<StudentEntity> modifiedStudents = new ArrayList<>();

		for (StudentEntity studentEntity : studentsOfClass) {
			if (!studentTeacherCourseRepository.existsByStudentAndTeacherCourse(studentEntity, teacherCourse)) {
				StudentTeacherCourseEntity stce = new StudentTeacherCourseEntity();
				stce.setStudent(studentEntity);
				stce.setTeacherCourse(teacherCourse);
				studentTeacherCourseRepository.save(stce);
				modifiedStudents.add(studentEntity);
			}
		}

		return modifiedStudents;

	}
	
	@Override
	public Boolean isActive(Integer classId) {
		if(classRepository.existsById(classId)) {
			ClassEntity clazz = classRepository.findById(classId).get();
			//if(clazz.getDeleted().equals(true)) {
			//	return false;
			//}
			return true;
		}
		return false;
	}





}
