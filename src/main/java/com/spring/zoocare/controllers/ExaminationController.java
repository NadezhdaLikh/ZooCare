package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.ExaminationRequest;
import com.spring.zoocare.models.dtos.responses.ExaminationResponse;
import com.spring.zoocare.models.enums.ExaminationType;
import com.spring.zoocare.services.ExaminationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/examinations")
@RequiredArgsConstructor
@Tag(name = "Ветеринарные осмотры")
public class ExaminationController {

    private final ExaminationService examinationService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить осмотр по его id")
    public ExaminationResponse getExamination(@PathVariable Integer id) {
        return examinationService.getExamination(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех осмотров (с фильтрацией по диагнозу или рецепту от ветеринара)")
    public Page<ExaminationResponse> getAllExaminations(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortParam,
                                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                        @RequestParam(required = false) String filter) {
        return examinationService.getAllExaminationsFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/by-animal/{animalId}")
    @Operation(summary = "Получить список всех осмотров животного с указанным id")
    public Page<ExaminationResponse> getExaminationsByAnimalId(@PathVariable Integer animalId, @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                                               @RequestParam(defaultValue = "id") String sortParam,
                                                               @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect) {
        return examinationService.getAllExaminationsByAnimalId(page, pageSize, sortParam, sortDirect, animalId);
    }

    @GetMapping("/by-vet/{vetId}")
    @Operation(summary = "Получить список всех осмотров, проведенных ветеринаром с указанным id")
    public Page<ExaminationResponse> getExaminationsByVetId(@PathVariable Integer vetId, @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortParam,
                                                            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect) {
        return examinationService.getAllExaminationsByVetId(page, pageSize, sortParam, sortDirect, vetId);
    }

    @GetMapping("/by-type")
    @Operation(summary = "Получить список всех осмотров указанного типа")
    public Page<ExaminationResponse> getExaminationsByExaminationType(@RequestParam ExaminationType examinationType,
                                                                      @RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "id") String sortParam,
                                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect) {
        return examinationService.getAllExaminationsByExaminationType(page, pageSize, sortParam, sortDirect, examinationType);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Добавить новый осмотр")
    public ExaminationResponse addExamination(@RequestBody @Valid ExaminationRequest examinationRequest) {
        return examinationService.addExamination(examinationRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Обновить осмотр по его id")
    public ExaminationResponse updateExamination(@PathVariable Integer id, @RequestBody @Valid ExaminationRequest examinationRequest) {
        return examinationService.updateExamination(id, examinationRequest);
    }
}
