package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.ChangePasswordRequest;
import com.spring.zoocare.models.dtos.requests.EmployeeRequest;
import com.spring.zoocare.models.dtos.responses.EmployeeResponse;
import com.spring.zoocare.models.enums.Role;
import com.spring.zoocare.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Сотрудники зоопарка")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить сотрудника по его id")
    public EmployeeResponse getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployee(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех сотрудников (с фильтрацией по имени, фамилии, занимаемой должности или email")
    public Page<EmployeeResponse> getAllEmployeesFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortParam,
                                                          @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                          @RequestParam(required = false) String filter) {
        return employeeService.getAllEmployeesFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/by-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получить список всех сотрудников указанной роли")
    public Page<EmployeeResponse> getAllEmployeesByRole(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortParam,
                                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                        @RequestParam Role role) {
        return employeeService.getAllEmployeesByRole(page, pageSize, sortParam, sortDirect, role);
    }

    @GetMapping("/is-on-staff")
    @Operation(summary = "Получить список всех сотрудников, числящихся/не числящихся на данный момент в штате зоопарка")
    public Page<EmployeeResponse> getAllEmployeesByIsOnStaff(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortParam,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                             @RequestParam Boolean isOnStaff) {
        return employeeService.getAllEmployeesByIsOnStaff(page, pageSize, sortParam, sortDirect, isOnStaff);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Добавить нового сотрудника")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse addEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        return employeeService.addEmployee(employeeRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновить сотрудника по его id")
    public EmployeeResponse updateEmployee(@PathVariable Integer id, @RequestBody @Valid EmployeeRequest employeeRequest) {
        return employeeService.updateEmployee(id, employeeRequest);
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Изменить пароль")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        employeeService.updatePassword(changePasswordRequest);
        return ResponseEntity.ok("Пароль успешно изменен!");
    }
}
