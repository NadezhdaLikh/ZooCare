package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.Enclosure;
import com.spring.zoocare.models.enums.EnclosureType;
import com.spring.zoocare.models.enums.ZooZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnclosureRepository extends JpaRepository<Enclosure, Integer> {
    @Query("select e from Enclosure e where e.zooZone = :zooZone")
    Page<Enclosure> findAllByZooZone(Pageable pageable, @Param("zooZone")ZooZone zooZone);

    @Query("select e from Enclosure e where e.enclosureType = :enclosureType")
    Page<Enclosure> findAllByEnclosureType(Pageable pageable, @Param("enclosureType")EnclosureType enclosureType);

    @Query("select e from Enclosure e where e.isNowInhabitable = :isNowInhabitable")
    Page<Enclosure> findAllByIsNowInhabitable(Pageable pageable, @Param("isNowInhabitable")Boolean isNowInhabitable);
}
