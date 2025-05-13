package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.*;
import com.spring.zoocare.models.enums.ExaminationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExaminationRepository extends JpaRepository<Examination, Integer> {
    @Query("select e from Examination e where e.diagnosis like %:filter% or e.prescription like %:filter%")
    Page<Examination> findAllFiltered(Pageable pageable, @Param("filter")String filter);

    @Query("select e from Examination e where e.animal = :animal")
    Page<Examination> findAllByAnimal(Pageable pageable, @Param("animal")Animal animal);

    @Query("select e from Examination e where e.vet = :vet")
    Page<Examination> findAllByVet(Pageable pageable, @Param("vet")Employee vet);

    @Query("select e from Examination e where e.examinationType = :examinationType")
    Page<Examination> findAllByExaminationType(Pageable pageable, @Param("examinationType")ExaminationType examinationType);
}
