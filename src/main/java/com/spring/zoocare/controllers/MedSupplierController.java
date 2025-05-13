package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.MedSupplierRequest;
import com.spring.zoocare.models.dtos.responses.MedSupplierResponse;
import com.spring.zoocare.services.MedSupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zoocare-api/v1/medication-suppliers")
@Tag(name = "Поставщики медицинских препаратов")
public class MedSupplierController {

    private final MedSupplierService medSupplierService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить поставщика по его id")
    public MedSupplierResponse getMedSupplier(@PathVariable Integer id) {
        return medSupplierService.getMedSupplier(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех поставщиков (с фильтрацией по названию организации или корпоративному email)")
    public Page<MedSupplierResponse> getAllMedSuppliersFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "id") String sortParam,
                                                                @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                @RequestParam(required = false) String filter) {
        return medSupplierService.getAllMedSuppliersFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/currently-supplying")
    @Operation(summary = "Получить список всех поставщиков, поставляющих/не поставляющих зоопарку препараты на данный момент")
    public Page<MedSupplierResponse> getAllMedSuppliersIsNowSupplying(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "id") String sortParam,
                                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                      @RequestParam Boolean isNowSupplying) {
        return medSupplierService.getAllMedSuppliersIsNowSupplying(page, pageSize, sortParam, sortDirect, isNowSupplying);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "Добавить нового поставщика")
    public MedSupplierResponse addMedSupplier(@RequestBody @Valid MedSupplierRequest medSupplierRequest) {
        return medSupplierService.addMedSupplier(medSupplierRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "Обновить поставщика по его id")
    public MedSupplierResponse updateMedSupplier(@PathVariable Integer id, @RequestBody @Valid MedSupplierRequest medSupplierRequest) {
        return medSupplierService.updateMedSupplier(id, medSupplierRequest);
    }

    @PatchMapping("/{id}/declare-obsolete")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "По id поставщика обозначить, что он прекратил поставку препаратов")
    public MedSupplierResponse declareMedSupplierObsolete(@PathVariable Integer id) {
        return medSupplierService.declareMedSupplierObsolete(id);
    }
}

