package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.*;
import com.spring.zoocare.models.enums.Sex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedUsageRepository extends JpaRepository<MedUsage, MedUsageId> {
    List<MedUsage> findByMedUsageId_MedProcedure(MedProcedure medProcedure); // Все партии с медицинскими препаратами, использованные при проведении данной процедуры
    List<MedUsage> findByMedUsageId_BatchInStock(BatchInStock batchInStock); // Все процедуры, когда использовалась данная партия медицинского препарата
    List<MedUsage> findByMedUsageId_BatchInStock_Medication(Medication medication); // Все процедуры, когда использовался данный медицинский препарат

    @Query("select m from MedUsage m where m.medUsageId.medProcedure = :medProcedure")
    Page<MedUsage> findAllByMedProcedure(Pageable pageRequest, @Param("medProcedure")MedProcedure medProcedure); // Все партии с медицинскими препаратами, использованные при проведении данной процедуры

    @Query("select m from MedUsage m where m.medUsageId.batchInStock = :batchInStock")
    Page<MedUsage> findAllByBatchInStock(Pageable pageRequest, @Param("batchInStockId")BatchInStock batchInStock); // Все процедуры, когда использовалась данная партия медицинского препарата

    @Query("select m from MedUsage m where m.medUsageId.batchInStock.medication = :medication")
    Page<MedUsage> findAllByMedication(Pageable pageRequest, @Param("medication")Medication medication); // Все процедуры, когда использовался данный медицинский препарат
}
