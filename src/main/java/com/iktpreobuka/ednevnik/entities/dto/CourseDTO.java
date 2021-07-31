package com.iktpreobuka.ednevnik.entities.dto;


import com.iktpreobuka.ednevnik.entities.ESemester;

public class CourseDTO {

	private String name;
	private Integer weeklyHours;
	private ESemester semester;

	public CourseDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(Integer weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public ESemester getSemester() {
		return semester;
	}

	public void setSemester(ESemester semester) {
		this.semester = semester;
	}

	
}
