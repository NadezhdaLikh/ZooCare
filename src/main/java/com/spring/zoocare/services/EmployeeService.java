package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.BatchInStock;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.repositories.EmployeeRepository;
import com.spring.zoocare.models.dtos.requests.ChangePasswordRequest;
import com.spring.zoocare.models.dtos.requests.EmployeeRequest;
import com.spring.zoocare.models.dtos.responses.BatchInStockResponse;
import com.spring.zoocare.models.dtos.responses.EmployeeResponse;
import com.spring.zoocare.models.enums.Role;
import com.spring.zoocare.utils.PaginationUtils;
import com.spring.zoocare.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final ObjectMapper objectMapper;
    private final EmployeeRepository employeeRepository;
    private final PasswordService passwordService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public Employee findEmployeeById(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        return optionalEmployee.orElseThrow(() -> new CustomBackendException("Сотрудник с данным ID не найден.", HttpStatus.NOT_FOUND));
    }

    public Employee findEmployeeByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

        return optionalEmployee.orElseThrow(() -> new CustomBackendException("Сотрудник с данным адресом электронной почты не найден.", HttpStatus.NOT_FOUND));
    }

    public EmployeeResponse getEmployee(Integer id) {
        return customizeEmployeeResponse(findEmployeeById(id));
    }

    private Page<EmployeeResponse> composeEmployeeResponsePage(Pageable pageRequest, Page<Employee> employees) {
        List<EmployeeResponse> content = employees.getContent().stream()
                .map(this::customizeEmployeeResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, employees.getTotalElements());
    }

    public EmployeeResponse customizeEmployeeResponse(Employee employee1) {
        EmployeeResponse employeeResponse = objectMapper.convertValue(employee1, EmployeeResponse.class);

        Employee employee2 = findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        if (!employee2.getRole().equals(Role.ADMIN)) {
            employeeResponse.setRole(null);
        }

        return employeeResponse;
    }

    public Page<EmployeeResponse> getAllEmployeesFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by first name, last name, occupation or email
        Page<Employee> employees;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            employees = employeeRepository.findAllFiltered(pageRequest, filter);
        } else employees = employeeRepository.findAll(pageRequest);

        return composeEmployeeResponsePage(pageRequest, employees);
    }

    public Page<EmployeeResponse> getAllEmployeesByIsOnStaff(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isOnStaff) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Employee> employees = employeeRepository.findAllByIsOnStaff(pageRequest, isOnStaff);

        return composeEmployeeResponsePage(pageRequest, employees);
    }

    public Page<EmployeeResponse> getAllEmployeesByRole(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Role role) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Employee> employees = employeeRepository.findAllByRole(pageRequest, role);

        return composeEmployeeResponsePage(pageRequest, employees);
    }

    public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
        employeeRepository.findByEmail(employeeRequest.getEmail()).ifPresent(e -> {
            throw new CustomBackendException("Сотрудник с данным адресом электронной почты уже существует.", HttpStatus.CONFLICT);
        });

        Employee employee = objectMapper.convertValue(employeeRequest, Employee.class);
        employee.setIsOnStaff(true);
        createAndSendPassword(employee);

        return customizeEmployeeResponse(employeeRepository.save(employee));
    }

    private void createAndSendPassword(Employee employee) {
        String rawPassword = passwordService.generatePassword();
        emailService.sendPasswordEmail(employee.getEmail(), rawPassword);
        employee.setPassword(passwordEncoder.encode(rawPassword));
    }

    public EmployeeResponse updateEmployee(Integer id, EmployeeRequest employeeRequest) {
        Employee employee = findEmployeeById(id);

        employee.setFirstName(employeeRequest.getFirstName() == null ? employee.getFirstName() : employeeRequest.getFirstName());
        employee.setMiddleName(employeeRequest.getMiddleName() == null ? employee.getMiddleName() : employeeRequest.getMiddleName());
        employee.setLastName(employeeRequest.getLastName() == null ? employee.getLastName() : employeeRequest.getLastName());
        employee.setSex(employeeRequest.getSex() == null ? employee.getSex() : employeeRequest.getSex());
        employee.setBirthDate(employeeRequest.getBirthDate() == null ? employee.getBirthDate() : employeeRequest.getBirthDate());
        employee.setOccupation(employeeRequest.getOccupation() == null ? employee.getOccupation() : employeeRequest.getOccupation());
        employee.setWhenStartedWork(employeeRequest.getWhenStartedWork() == null ? employee.getWhenQuitWork() : employeeRequest.getWhenStartedWork());

        if (employeeRequest.getWhenQuitWork() != null) { // При увольнении сотрудника зоопарка
            employee.setWhenQuitWork(employeeRequest.getWhenQuitWork());
            employee.setIsOnStaff(false);
        } else employee.setIsOnStaff(employee.getIsOnStaff());

        employee.setPhoneNumber(employeeRequest.getPhoneNumber() == null ? employee.getPhoneNumber() : employeeRequest.getPhoneNumber());
        employee.setEmail(employeeRequest.getEmail() == null ? employee.getEmail() : employeeRequest.getEmail());
        employee.setRole(employeeRequest.getRole() == null ? employee.getRole() : employeeRequest.getRole());

        return customizeEmployeeResponse(employeeRepository.save(employee));
    }

    public void updatePassword(ChangePasswordRequest changePasswordRequest) {
        Employee employee = findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        validatePassword(changePasswordRequest.getNewPassword());

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), employee.getPassword())) { // Returns true if the raw password, after encoding, matches the encoded password from storage
            throw new CustomBackendException("Неверный текущий пароль.", HttpStatus.UNAUTHORIZED);
        }

        employee.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        employeeRepository.save(employee);
    }

    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&()_+=?]).{8,}$";

        if (!password.matches(passwordPattern)) {
            throw new CustomBackendException("Пароль должен быть не короче 8 символов и содержать как минимум одну цифру, одну строчную букву, одну заглавную букву и один специальный символ».", HttpStatus.BAD_REQUEST);
        }
    }
}

