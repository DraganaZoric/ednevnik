package com.iktpreobuka.ednevnik.entities.dto;


public class ClassDTO {

	private Integer classNumber;
	private Integer sectionNo;
	private Integer generation;

	public ClassDTO() {
		super();
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public Integer getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(Integer sectionNo) {
		this.sectionNo = sectionNo;
	}

	public Integer getGeneration() {
		return generation;
	}

	public void setGeneration(Integer generation) {
		this.generation = generation;
	}

}
