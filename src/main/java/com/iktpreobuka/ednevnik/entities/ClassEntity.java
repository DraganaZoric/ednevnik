package com.iktpreobuka.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "class")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "class", nullable = false)
	@NotNull(message = "Class number must be provided.")
	@Min(value = 1, message = "Class number must be greater than {value}.")
	@Max(value = 8, message = "Class number must be lesser than than {value}.")
	private Integer classNumber;

	@Column(name = "section", nullable = false)
	@NotNull(message = "Section number must be provided.")
	@Min(value = 1, message = "Section number must be greater than {value}.")
	@Max(value = 8, message = "Section number must be lesser than than {value}.")
	private Integer sectionNo;

	@Column(name = "generation", nullable = false)
	@NotNull(message = "Generation must be provided.")
	@Min(value = 1900, message = "Generation must be greater than {value}.")
	@Max(value = 2025, message = "Generation must be lesser than than {value}.")
	private Integer generation;

	@Version
	private Integer version;

	@JsonIgnore
	@OneToMany // (mappedBy = "attendingClass", cascade = CascadeType.REFRESH, fetch =
				// FetchType.LAZY)
	// @JsonBackReference
	protected List<StudentEntity> students = new ArrayList<>();

	public ClassEntity() {
		super();
	}

	public ClassEntity(Integer id,
			@NotNull(message = "Class number must be provided.") @Min(value = 1, message = "Class number must be greater than {value}.") @Max(value = 8, message = "Class number must be lesser than than {value}.") Integer classNumber,
			@NotNull(message = "Section number must be provided.") @Min(value = 1, message = "Section number must be greater than {value}.") @Max(value = 8, message = "Section number must be lesser than than {value}.") Integer sectionNo,
			@NotNull(message = "Generation must be provided.") @Min(value = 1900, message = "Generation must be greater than {value}.") @Max(value = 2025, message = "Generation must be lesser than than {value}.") Integer generation,
			Integer version, List<StudentEntity> students) {
		super();
		this.id = id;
		this.classNumber = classNumber;
		this.sectionNo = sectionNo;
		this.generation = generation;
		this.version = version;
		this.students = students;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

}
