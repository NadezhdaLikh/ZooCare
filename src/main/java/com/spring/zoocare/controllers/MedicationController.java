package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.MedicationRequest;
import com.spring.zoocare.models.dtos.responses.MedicationResponse;
import com.spring.zoocare.models.enums.MedCategory;
import com.spring.zoocare.services.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/medications")
@RequiredArgsConstructor
@Tag(name = "Медицинские препараты")
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить медицинский препарат по его id")
    public MedicationResponse getMedication(@PathVariable Integer id) {
        return medicationService.getMedication(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех медицинских препаратов (с фильтрацией по названию)")
    public Page<MedicationResponse> getAllMedicationsFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortParam,
                                                              @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                              @RequestParam(required = false) String filter) {
        return medicationService.getAllMedicationsFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/by-category")
    @Operation(summary = "Получить список всех медицинских препаратов указанной категории")
    public Page<MedicationResponse> getAllMedicationsByMedCategory(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                   @RequestParam MedCategory medCategory) {
        return medicationService.getAllMedicationsByMedCategory(page, pageSize, sortParam, sortDirect, medCategory);
    }

    @GetMapping("/currently-in-use")
    @Operation(summary = "Получить список всех медицинских препаратов, которые используются/не используются ветеринарами зоопарка на данный момент")
    public Page<MedicationResponse> getAllMedicationsByIsNowInUse(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                  @RequestParam Boolean isNowInUse) {
        return medicationService.getAllMedicationsByIsNowInUse(page, pageSize, sortParam, sortDirect, isNowInUse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Добавить новый медицинский препарат")
    public MedicationResponse addMedication(@RequestBody @Valid MedicationRequest medicationRequest) {
        return medicationService.addMedication(medicationRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Обновить медицинский препарат по его id")
    public MedicationResponse updateMedication(@PathVariable Integer id, @RequestBody @Valid MedicationRequest medicationRequest) {
        return medicationService.updateMedication(id, medicationRequest);
    }

    @PatchMapping("/{id}/declare-obsolete")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Обозначить медицинский препарат устаревшим по его id")
    public MedicationResponse declareMedicationObsolete(@PathVariable Integer id) {
        return medicationService.declareMedicationObsolete(id);
    }
}