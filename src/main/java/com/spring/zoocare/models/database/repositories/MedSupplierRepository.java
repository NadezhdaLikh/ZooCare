package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.MedSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedSupplierRepository extends JpaRepository<MedSupplier, Integer> {

    Optional<MedSupplier> findByOgrn(String ogrn);
    Optional<MedSupplier> findByInn(String inn);
    Optional<MedSupplier> findByKpp(String kpp);

    @Query("select m from MedSupplier m where m.name like %:filter% or m.email like %:filter%")
    Page<MedSupplier> findAllFiltered(Pageable pageable, @Param("filter")String filter);

    @Query("select m from MedSupplier m where m.isNowSupplying = :isNowSupplying")
    Page<MedSupplier> findAllByIsNowSupplying(Pageable pageable, @Param("isNowSupplying")Boolean isNowSupplying);
}
