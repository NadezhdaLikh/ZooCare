package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.BatchInStock;
import com.spring.zoocare.models.database.entities.MedSupplier;
import com.spring.zoocare.models.database.entities.MedUsage;
import com.spring.zoocare.models.database.entities.Medication;
import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.models.enums.DosageForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BatchInStockRepository extends JpaRepository<BatchInStock, Integer> {
    @Query("select b from BatchInStock b where b.deliveredFrom like %:filter%")
    Page<BatchInStock> findAllFiltered(Pageable pageable, @Param("filter")String filter);

    @Query("select b from BatchInStock b where b.medication = :medication")
    Page<BatchInStock> findAllByMedication(Pageable pageable, @Param("medication")Medication medication);

    @Query("select b from BatchInStock b where b.medSupplier = :medSupplier")
    Page<BatchInStock> findAllByMedSupplier(Pageable pageable, @Param("medSupplier")MedSupplier medSupplier);

    @Query("select b from BatchInStock b where b.dosageForm = :dosageForm")
    Page<BatchInStock> findAllByDosageForm(Pageable pageable, @Param("dosageForm")DosageForm dosageForm);

    @Query("select b from BatchInStock b where b.administrationRoute = :administrationRoute")
    Page<BatchInStock> findAllByAdministrationRoute(Pageable pageRequest, @Param("administrationRoute") AdministrationRoute administrationRoute);

    @Query("select b from BatchInStock b where b.deliveredOn = :deliveredOn")
    Page<BatchInStock> findAllByDeliveredOn(Pageable pageable, @Param("deliveredOn")LocalDate deliveredOn);

    @Query("select b from BatchInStock b where b.expirationDate = :expirationDate")
    Page<BatchInStock> findAllByExpirationDate(Pageable pageable, @Param("expirationDate")LocalDate expirationDate);
}
