package com.spring.zoocare.config;

import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.repositories.EmployeeRepository;
import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminCredentials implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        boolean adminExists = employeeRepository.existsByRole(Role.ADMIN);

        if (adminExists) {
            System.out.println("Admin already exists. Skipping creation.");
            return;
        }

        Employee admin = new Employee();
        admin.setFirstName("admin's_first_name");
        admin.setMiddleName("admin's_middle_name");
        admin.setLastName("admin's_last_name");

        // Replace with Sex.MALE or Sex.FEMALE depending on admin's sex
        admin.setSex(Sex.FEMALE);
        
        // Replace with admin's date of birth (format: yyyy-mm-dd)
        admin.setBirthDate(LocalDate.of(2000, 1, 1));
        
        admin.setOccupation("admin's_occupation");
        admin.setWhenStartedWork(LocalDate.now());
        admin.setIsOnStaff(true);
        admin.setPhoneNumber("admin's_phone_number");
        admin.setEmail("admin@example.com");

        String encodedPassword = passwordEncoder.encode("admin123");
        admin.setPassword(encodedPassword);
        admin.setRole(Role.ADMIN);

        employeeRepository.save(admin);

        System.out.println("Admin created successfully.");
    }
}
