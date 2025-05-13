package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.*;
import com.spring.zoocare.models.database.repositories.BatchInStockRepository;
import com.spring.zoocare.models.dtos.requests.BatchInStockRequest;
import com.spring.zoocare.models.dtos.responses.AnimalResponse;
import com.spring.zoocare.models.dtos.responses.BatchInStockResponse;
import com.spring.zoocare.models.dtos.responses.MedUsageResponse;
import com.spring.zoocare.models.dtos.responses.MedicationResponse;
import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.models.enums.DosageForm;
import com.spring.zoocare.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchInStockService {

    private final ObjectMapper objectMapper;
    private final BatchInStockRepository batchInStockRepository;
    private final MedicationService medicationService;
    private final MedSupplierService medSupplierService;

    public BatchInStock findBatchInStockById(Integer id) {
        Optional<BatchInStock> optionalBatchInStock = batchInStockRepository.findById(id);

        return optionalBatchInStock.orElseThrow(() -> new CustomBackendException("Партия с данным id не найдена на складе.", HttpStatus.NOT_FOUND));
    }

    public BatchInStockResponse getBatchInStock(Integer id) {
        return customizeBatchInStockResponse(findBatchInStockById(id));
    }

    public Page<BatchInStockResponse> getAllBathesInStockFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by the place from which batches were delivered
        Page<BatchInStock> batchInStocks;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            batchInStocks = batchInStockRepository.findAllFiltered(pageRequest, filter);
        } else batchInStocks = batchInStockRepository.findAll(pageRequest);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public BatchInStockResponse customizeBatchInStockResponse(BatchInStock batchInStock) {
        BatchInStockResponse batchInStockResponse = objectMapper.convertValue(batchInStock, BatchInStockResponse.class);

        MedicationResponse medicationResponse = objectMapper.convertValue(batchInStock.getMedication(), MedicationResponse.class);
        batchInStockResponse.setMedicationId(medicationResponse.getId());
        batchInStockResponse.setMedicationResponse(medicationResponse);

        batchInStockResponse.setMedSupplierId(batchInStock.getMedSupplier().getId());

        return batchInStockResponse;
    }

    private Page<BatchInStockResponse> composeBatchInStockResponsePage(Pageable pageRequest, Page<BatchInStock> batchInStocks) {
        List<BatchInStockResponse> content = batchInStocks.getContent().stream()
                .map(this::customizeBatchInStockResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, batchInStocks.getTotalElements());
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByMedicationId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer medicationId) {
        Medication medication = medicationService.findMedicationById(medicationId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByMedication(pageRequest, medication);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByMedSupplierId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer medSupplierId) {
        MedSupplier medSupplier = medSupplierService.findMedSupplierById(medSupplierId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByMedSupplier(pageRequest, medSupplier);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByDosageForm(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, DosageForm dosageForm) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByDosageForm(pageRequest, dosageForm);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByAdministrationRoute(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, AdministrationRoute administrationRoute) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByAdministrationRoute(pageRequest, administrationRoute);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByDeliveredOn(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, LocalDate deliveredOn) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByDeliveredOn(pageRequest, deliveredOn);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public Page<BatchInStockResponse> getAllBatchesInStockByExpirationDate(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, LocalDate expirationDate) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<BatchInStock> batchInStocks = batchInStockRepository.findAllByExpirationDate(pageRequest, expirationDate);

        return composeBatchInStockResponsePage(pageRequest, batchInStocks);
    }

    public BatchInStockResponse addBatchInStock(BatchInStockRequest batchInStockRequest) {
        BatchInStock batchInStock = objectMapper.convertValue(batchInStockRequest, BatchInStock.class);

        Medication medication = medicationService.findMedicationById(batchInStockRequest.getMedicationId());
        batchInStock.setMedication(medication);

        MedSupplier medSupplier = medSupplierService.findMedSupplierById(batchInStockRequest.getMedSupplierId());
        batchInStock.setMedSupplier(medSupplier);

        batchInStockRepository.save(batchInStock);

        return customizeBatchInStockResponse(batchInStock);
    }

    public BatchInStockResponse updateBatchInStock(Integer id, BatchInStockRequest batchInStockRequest) {
        BatchInStock batchInStock = findBatchInStockById(id);

        batchInStock.setMedication(batchInStockRequest.getMedicationId() != null
                ? medicationService.findMedicationById(batchInStockRequest.getMedicationId())
                : batchInStock.getMedication());

        batchInStock.setMedSupplier(batchInStockRequest.getMedSupplierId() != null
                ? medSupplierService.findMedSupplierById(batchInStockRequest.getMedSupplierId())
                : batchInStock.getMedSupplier());

        batchInStock.setDosageForm(batchInStockRequest.getDosageForm() != null
                ? batchInStockRequest.getDosageForm()
                : batchInStock.getDosageForm());

        batchInStock.setDosageUnits(batchInStockRequest.getDosageUnits() != null
                ? batchInStockRequest.getDosageUnits()
                : batchInStock.getDosageUnits());

        batchInStock.setAdministrationRoute(batchInStockRequest.getAdministrationRoute() != null
                ? batchInStockRequest.getAdministrationRoute()
                : batchInStock.getAdministrationRoute());

        batchInStock.setTotalNumOfUnits(batchInStockRequest.getTotalNumOfUnits() != null
                ? batchInStockRequest.getTotalNumOfUnits()
                : batchInStock.getTotalNumOfUnits());

        batchInStock.setDeliveredOn(batchInStockRequest.getDeliveredOn() != null
                ? batchInStockRequest.getDeliveredOn()
                : batchInStock.getDeliveredOn());

        batchInStock.setDeliveredFrom(batchInStockRequest.getDeliveredFrom() != null
                ? batchInStockRequest.getDeliveredFrom()
                : batchInStock.getDeliveredFrom());

        batchInStock.setExpirationDate(batchInStockRequest.getExpirationDate() != null
                ? batchInStockRequest.getExpirationDate()
                : batchInStock.getExpirationDate());

        batchInStockRepository.save(batchInStock);

        return customizeBatchInStockResponse(batchInStock);
    }

    public void updateBatchInStockTotalNumOfUnits(BatchInStock batchInStock) {
        batchInStockRepository.save(batchInStock);
    }
}
