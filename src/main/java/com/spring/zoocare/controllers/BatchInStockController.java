package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.BatchInStockRequest;
import com.spring.zoocare.models.dtos.responses.BatchInStockResponse;
import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.models.enums.DosageForm;
import com.spring.zoocare.services.BatchInStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/zoocare-api/v1/batches-in-stock")
@RequiredArgsConstructor
@Tag(name = "Партии медицинских препаратов на складе")
public class BatchInStockController {

    private final BatchInStockService batchInStockService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить партию препарата по ее id")
    public BatchInStockResponse getBatchInStockById(@PathVariable Integer id) {
        return batchInStockService.getBatchInStock(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех партий препаратов на складе (с фильтрацией по месту отправки поставки")
    public Page<BatchInStockResponse> getAllBatchesInStockFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                   @RequestParam(required = false) String filter) {
        return batchInStockService.getAllBathesInStockFiltered(page, pageSize, sortParam, sortDirection, filter);
    }

    @GetMapping("/by-medication/{medicationId}")
    @Operation(summary = "Получить список всех имеющихся на складе партий препарата с указанным id")
    public Page<BatchInStockResponse> getAllBatchesByMedicationId(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                  @PathVariable Integer medicationId) {
        return batchInStockService.getAllBatchesInStockByMedicationId(page, pageSize, sortParam, sortDirection, medicationId);
    }

    @GetMapping("/by-supplier/{medSupplierId}")
    @Operation(summary = "Получить список всех партий препаратов, поставляемых поставщиком с указанным id")
    public Page<BatchInStockResponse> getAllBatchesByMedSupplierId(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                   @PathVariable Integer medSupplierId) {
        return batchInStockService.getAllBatchesInStockByMedSupplierId(page, pageSize, sortParam, sortDirection, medSupplierId);
    }

    @GetMapping("/by-dosage-form")
    @Operation(summary = "Получить список всех партий с указанной лекарственной формой")
    public Page<BatchInStockResponse> getAllBatchesByDosageForm(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "id") String sortParam,
                                                                @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                @RequestParam DosageForm dosageForm) {
        return batchInStockService.getAllBatchesInStockByDosageForm(page, pageSize, sortParam, sortDirection, dosageForm);
    }

    @GetMapping("/by-administration-route")
    @Operation(summary = "Получить список всех партий с указанным способом введения лекарственного препарата")
    public Page<BatchInStockResponse> getAllBatchesByAdministrationRoute(@RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(defaultValue = "id") String sortParam,
                                                                         @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                         @RequestParam AdministrationRoute administrationRoute) {
        return batchInStockService.getAllBatchesInStockByAdministrationRoute(page, pageSize, sortParam, sortDirection, administrationRoute);
    }

    @GetMapping("/by-delivery-date")
    @Operation(summary = "Получить список всех партий с указанной датой поставки")
    public Page<BatchInStockResponse> getAllBatchesByDeliveredOn(@RequestParam(defaultValue = "0") Integer page,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(defaultValue = "id") String sortParam,
                                                                 @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                 @RequestParam LocalDate deliveredOn) {
        return batchInStockService.getAllBatchesInStockByDeliveredOn(page, pageSize, sortParam, sortDirection, deliveredOn);
    }

    @GetMapping("/by-expiration-date")
    @Operation(summary = "Получить список всех партий с указанной датой истечения срока годности")
    public Page<BatchInStockResponse> getAllBatchesByExpirationDate(@RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortParam,
                                                                    @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                                    @RequestParam LocalDate expirationDate) {
        return batchInStockService.getAllBatchesInStockByExpirationDate(page, pageSize, sortParam, sortDirection, expirationDate);
    }

    @PostMapping
    @Operation(summary = "Добавить новую партию")
    @PreAuthorize("hasAuthority('VET')")
    @ResponseStatus(HttpStatus.CREATED)
    public BatchInStockResponse addBatchInStock(@RequestBody @Valid BatchInStockRequest batchInStockRequest) {
        return batchInStockService.addBatchInStock(batchInStockRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Обновить партию на складе по ее id")
    public BatchInStockResponse updateBatchInStock(@PathVariable Integer id, @RequestBody @Valid BatchInStockRequest batchInStockRequest) {
        return batchInStockService.updateBatchInStock(id, batchInStockRequest);
    }
}
