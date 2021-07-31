package com.iktpreobuka.ednevnik.entities.dto;

import java.time.LocalDate;


public class GradeDTO {

	private Integer grade;

	protected LocalDate date;

	public GradeDTO() {
		super();
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
