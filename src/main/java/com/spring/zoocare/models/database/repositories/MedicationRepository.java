package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.Medication;
import com.spring.zoocare.models.enums.MedCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    @Query("select m from Medication m where m.name like %:filter%")
    Page<Medication> findAllFiltered(Pageable pageable, @Param("filter")String filter);

    @Query("select m from Medication m where m.isNowInUse = :isNowInUse")
    Page<Medication> findAllByIsNowInUse(Pageable pageable, @Param("isNowInUse")Boolean isNowInUse);

    @Query("select m from Medication m where m.medCategory = :medCategory")
    Page<Medication> findAllByMedCategory(Pageable pageable, @Param("medCategory")MedCategory medCategory);
}
