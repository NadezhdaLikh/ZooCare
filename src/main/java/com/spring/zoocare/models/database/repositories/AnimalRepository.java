package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.Animal;
import com.spring.zoocare.models.database.entities.Enclosure;
import com.spring.zoocare.models.enums.HealthStatus;
import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Species;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    @Query("select a from Animal a where a.name like %:filter% or a.subspecies like %:filter% or a.placeOfBirth like %:filter%")
    Page<Animal> findAllFiltered(Pageable pageRequest, @Param("filter")String filter);

    @Query("select a from Animal a where a.enclosure = :enclosure")
    Page<Animal> findAllByEnclosure(Pageable pageable, @Param("enclosure")Enclosure enclosure);

    @Query("select a from Animal a where a.species = :species")
    Page<Animal> findAllBySpecies(Pageable pageRequest, @Param("species")Species species);

    @Query("select a from Animal a where a.sex = :sex")
    Page<Animal> findAllBySex(Pageable pageRequest, @Param("sex") Sex sex);

    @Query("select a from Animal a where a.bornInCaptivity = :bornInCaptivity")
    Page<Animal> findAllByBornInCaptivity(Pageable pageRequest, @Param("bornInCaptivity")Boolean bornInCaptivity);

    @Query("select a from Animal a where a.healthStatus = :healthStatus")
    Page<Animal> findAllByHealthStatus(Pageable pageRequest, @Param("healthStatus")HealthStatus healthStatus);

    @Query("select a from Animal a where a.hasChronicCondition = :hasChronicCondition")
    Page<Animal> findAllByHasChronicCondition(Pageable pageRequest, @Param("hasChronicCondition")Boolean hasChronicCondition);

    @Query("select a from Animal a where a.isQuarantined = :isQuarantined")
    Page<Animal> findAllByIsQuarantined(Pageable pageRequest, @Param("isQuarantined")Boolean isQuarantined);

}
