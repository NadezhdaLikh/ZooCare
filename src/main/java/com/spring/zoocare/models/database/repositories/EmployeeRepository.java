package com.spring.zoocare.models.database.repositories;

import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByRole(Role role);

    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName like %:filter% or e.lastName like %:filter% or e.occupation like %:filter% or e.email like %:filter%")
    Page<Employee> findAllFiltered(Pageable pageable, @Param("filter")String filter);

    @Query("select e from Employee e where e.isOnStaff = :isOnStaff")
    Page<Employee> findAllByIsOnStaff(Pageable pageable, @Param("isOnStaff")Boolean isOnStaff);

    @Query("select e from Employee e where e.role = :role")
    Page<Employee> findAllByRole(Pageable pageable, @Param("role")Role role);
}
