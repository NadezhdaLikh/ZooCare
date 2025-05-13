package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.Animal;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.entities.MedProcedure;
import com.spring.zoocare.models.enums.ProcedureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedProcedureRepository extends JpaRepository<MedProcedure, Integer> {
    @Query("select m from MedProcedure m where m.name like %:filter% or m.description like %:filter%")
    Page<MedProcedure> findAllFiltered(Pageable pageRequest, @Param("filter")String filter);

    @Query("select m from MedProcedure m where m.animal = :animal")
    Page<MedProcedure> findAllByAnimal(Pageable pageRequest, @Param("animal")Animal animal);

    @Query("select m from MedProcedure m where m.vet = :vet")
    Page<MedProcedure> findAllByVet(Pageable pageRequest, @Param("vet")Employee vet);

    @Query("select m from MedProcedure m where m.procedureType = :procedureType")
    Page<MedProcedure> findAllByProcedureType(Pageable pageRequest, @Param("procedureType")ProcedureType procedureType);
}
