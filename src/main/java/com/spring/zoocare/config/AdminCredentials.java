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
        admin.setFirstName("Надежда");
        admin.setMiddleName("Владиславовна");
        admin.setLastName("Лиханова");
        admin.setSex(Sex.FEMALE);
        admin.setBirthDate(LocalDate.of(2001, 6, 28));
        admin.setOccupation("Администратор веб-приложения, главный IT-специалист");
        admin.setWhenStartedWork(LocalDate.now());
        admin.setIsOnStaff(true);
        admin.setPhoneNumber("01234567890");
        admin.setEmail("nadezhda01likhanova@gmail.com");

        String encodedPassword = passwordEncoder.encode("admin_1234");
        admin.setPassword(encodedPassword);
        admin.setRole(Role.ADMIN);

        employeeRepository.save(admin);

        System.out.println("Admin created successfully.");
    }
}
