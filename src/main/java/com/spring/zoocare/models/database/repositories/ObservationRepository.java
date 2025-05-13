package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ObservationRepository extends JpaRepository<Observation, Integer> {

    @Query("select o from Observation o where o.message like %:filter% or o.vetFeedback like %:filter%")
    Page<Observation> findAllFiltered(Pageable pageable, @Param("filter") String filter);

    @Query("select o from Observation o where o.animal = :animal")
    Page<Observation> findAllByAnimal(Pageable pageRequest, @Param("animal")Animal animal);

    @Query("select o from Observation o where o.vet = :vet")
    Page<Observation> findAllByVet(Pageable pageRequest, @Param("vet")Employee vet);

    @Query("select o from Observation o where o.zookeeper = :zookeeper")
    Page<Observation> findAllByZookeeper(Pageable pageRequest, @Param("zookeeper")Employee zookeeper);

    @Query("select o from Observation o where o.madeOn = :madeOn")
    Page<Observation> findAllByMadeOn(Pageable pageRequest, @Param("madeOn")LocalDate madeOn);

    @Query("select o from Observation o where o.isUrgent = :isUrgent")
    Page<Observation> findAllByIsUrgent(Pageable pageRequest, @Param("isUrgent")Boolean isUrgent);

    @Query("select o from Observation o where o.isCheckedByVet = :isCheckedByVet")
    Page<Observation> findAllByIsCheckedByVet(Pageable pageRequest, @Param("isCheckedByVet")Boolean isCheckedByVet);

}