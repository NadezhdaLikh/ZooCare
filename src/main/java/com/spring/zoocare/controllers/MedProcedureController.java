package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.MedProcedureRequest;
import com.spring.zoocare.models.dtos.responses.MedProcedureResponse;
import com.spring.zoocare.models.enums.ProcedureType;
import com.spring.zoocare.services.MedProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/medical-procedures")
@RequiredArgsConstructor
@Tag(name = "Медицинские процедуры")
public class MedProcedureController {

    private final MedProcedureService medProcedureService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить медицинскую процедуру по ее id")
    public MedProcedureResponse getMedProcedure(@PathVariable Integer id) {
        return medProcedureService.getMedProcedure(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех процедур (с фильтрацией по названию или краткому описанию)")
    public Page<MedProcedureResponse> getAllMedProceduresFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                  @RequestParam(required = false) String filter) {
        return medProcedureService.getAllMedProceduresFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/by-animal/{animalId}")
    @Operation(summary = "По id животного получить список всех процедур, которые над ним проводились")
    public Page<MedProcedureResponse> getAllMedProceduresByAnimalId(@RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortParam,
                                                                    @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                    @PathVariable Integer animalId) {
        return medProcedureService.getAllMedProceduresByAnimalId(page, pageSize, sortParam, sortDirect, animalId);
    }

    @GetMapping("/by-vet/{vetId}")
    @Operation(summary = "По id ветеринара получить список всех процедур, за которые он был назначен ответственным")
    public Page<MedProcedureResponse> getAllMedProceduresByVetId(@RequestParam(defaultValue = "0") Integer page,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(defaultValue = "id") String sortParam,
                                                                 @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                 @PathVariable Integer vetId) {
        return medProcedureService.getAllMedProceduresByVetId(page, pageSize, sortParam, sortDirect, vetId);
    }

    @GetMapping("/by-type")
    @Operation(summary = "Получить список всех процедур указанного типа")
    public Page<MedProcedureResponse> getAllMedProceduresByProcedureType(@RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(defaultValue = "id") String sortParam,
                                                                         @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                         @RequestParam ProcedureType procedureType) {
        return medProcedureService.getAllMedProceduresByProcedureType(page, pageSize, sortParam, sortDirect, procedureType);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Добавить новую процедуру")
    public MedProcedureResponse addMedProcedure(@RequestBody @Valid MedProcedureRequest medProcedureRequest) {
        return medProcedureService.addMedProcedure(medProcedureRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Обновить процедуру по ее id")
    public MedProcedureResponse updateMedProcedure(@PathVariable Integer id, @RequestBody @Valid MedProcedureRequest medProcedureRequest) {
        return medProcedureService.updateMedProcedure(id, medProcedureRequest);
    }
}

