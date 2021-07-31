package com.iktpreobuka.ednevnik.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.ednevnik.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {


	Optional<TeacherEntity> findByEmail(String email);
}
