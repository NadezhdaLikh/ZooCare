package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.MedUsageRequest;
import com.spring.zoocare.models.dtos.responses.MedSupplierResponse;
import com.spring.zoocare.models.dtos.responses.MedUsageResponse;
import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.services.MedUsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/medication-usage")
@RequiredArgsConstructor
@Tag(name = "Сведения о расходе медицинских препаратов")
public class MedUsageController {

    private final MedUsageService medUsageService;

    @GetMapping("/by-procedure-and-batch/{medProcedureId}/{batchInStockId}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "По id процедуры и id партии с препаратом на складе получить сведения о расходе этого препарата за время данной процедуры")
    public MedUsageResponse getMedUsageByMedProcedureAndBatchIds(@PathVariable Integer medProcedureId, @PathVariable Integer batchInStockId) {
        return medUsageService.getMedUsageByMedProcedureAndBatchIds(medProcedureId, batchInStockId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "Получить сведения о расходе препаратов на складе за каждую проведенную процедуру")
    public Page<MedUsageResponse> getAllMedUsages(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect) {
        return medUsageService.getAllMedUsages(page, pageSize, sortParam, sortDirect);
    }

    @GetMapping("/by-procedure/{medProcedureId}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "По id процедуры получить сведения о расходе всех препаратов, использованных за время этой процедуры")
    public Page<MedUsageResponse> getAllMedUsagesByMedProcedureId(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                  @PathVariable Integer medProcedureId) {
        return medUsageService.getAllMedUsagesByMedProcedureId(page, pageSize, sortParam, sortDirection, medProcedureId);
    }

    @GetMapping("/by-batch/{batchInStockId}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "По id партии со склада получить сведения о расходе препарата из этой партии за время каждой процедуры, когда данный препарат использовался")
    public Page<MedUsageResponse> getAllMedUsagesByBatchInStockId(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                  @PathVariable Integer batchInStockId) {
        return medUsageService.getAllMedUsagesByBatchInStockId(page, pageSize, sortParam, sortDirection, batchInStockId);
    }

    @GetMapping("/by-medication/{medicationId}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "По id препарата получить сведения о его расходе за время каждой процедуры, когда этот препарат использовался")
    public Page<MedUsageResponse> getAllMedUsagesByMedicationId(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "id") String sortParam,
                                                                @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                @PathVariable Integer medicationId) {
        return medUsageService.getAllMedUsagesByMedicationId(page, pageSize, sortParam, sortDirection, medicationId);
    }

    @PostMapping("/{medProcedureId}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "По id процедуры добавить запись о расходе препарата из партии на складе")
    public MedUsageResponse addMedUsage(@PathVariable Integer medProcedureId, @RequestBody @Valid MedUsageRequest medUsageRequest) {
        return medUsageService.addMedUsage(medProcedureId, medUsageRequest);
    }

    @PutMapping("/{medProcedureId}/{batchInStockId}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "По id процедуры и id препарата обновить запись о расходе этого препарата за время данной процедуры")
    public MedUsageResponse updateMedUsage(@PathVariable Integer medProcedureId,
                                           @PathVariable Integer batchInStockId,
                                           @RequestBody @Valid MedUsageRequest medUsageRequest) {
        return medUsageService.updateMedUsage(medProcedureId, batchInStockId, medUsageRequest);
    }

    @DeleteMapping("/{medProcedureId}/{batchInStockId}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "По id процедуры и id партии со склада удалить запись о расходе препарата из этой партии за время данной процедуры")
    public void deleteMedUsage(@PathVariable Integer medProcedureId, @PathVariable Integer batchInStockId) {
        medUsageService.deleteMedUsage(medProcedureId, batchInStockId);
    }
}
