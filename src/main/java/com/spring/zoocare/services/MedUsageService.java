package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.*;
import com.spring.zoocare.models.database.repositories.MedUsageRepository;
import com.spring.zoocare.models.dtos.requests.MedUsageRequest;
import com.spring.zoocare.models.dtos.responses.BatchInStockResponse;
import com.spring.zoocare.models.dtos.responses.MedProcedureResponse;
import com.spring.zoocare.models.dtos.responses.MedUsageResponse;
import com.spring.zoocare.utils.PaginationUtils;
import com.spring.zoocare.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedUsageService {

    private final ObjectMapper objectMapper;
    private final MedUsageRepository medUsageRepository;
    private final MedicationService medicationService;
    private final BatchInStockService batchInStockService;
    private final MedProcedureService medProcedureService;
    private final EmployeeService employeeService;

    private MedUsage findMedUsageByMedUsageId(MedUsageId medUsageId) {
        Optional<MedUsage> optionalMedUsage = medUsageRepository.findById(medUsageId);

        String errorMessage = String.format("Запись об использовании партии c id %d медицинского препарата c id %d при проведении медицинской процедуры c id %d не найдена.",
                medUsageId.getBatchInStock().getId(),
                medUsageId.getBatchInStock().getMedication().getId(),
                medUsageId.getMedProcedure().getId());

        return optionalMedUsage.orElseThrow(() -> new CustomBackendException(errorMessage, HttpStatus.NOT_FOUND));
    }

    private <T> void validatePageNotEmpty(Page<T> page, String errorMessage) {
        if (page.isEmpty()) {
            throw new CustomBackendException(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /*private List<MedUsage> findMedUsageByMedProcedureId(Integer medProcedureId) {
        MedProcedure medProcedure = medProcedureService.findMedProcedureById(medProcedureId);
        List<MedUsage> medUsageListByMedProcedureId = medUsageRepository.findByMedUsageId_MedProcedure(medProcedure);

        if (medUsageListByMedProcedureId.isEmpty()) {
            throw  new CustomBackendException("При проведении данной процедуры никакие медицинские препараты не использовались.", HttpStatus.NOT_FOUND);
        }

        return medUsageListByMedProcedureId;
    }

    private List<MedUsage> findMedUsageByMedicationId(Integer medicationId) {
        Medication medication = medicationService.findMedicationById(medicationId);
        List<MedUsage> medUsageListByMedicationId = medUsageRepository.findByMedUsageId_BatchInStock_Medication(medication);

        if (medUsageListByMedicationId.isEmpty()) {
            throw new CustomBackendException("Данный препарат еще ни разу не был использован при проведении медицинской процедуры.", HttpStatus.NOT_FOUND);
        }

        return medUsageListByMedicationId;
    }

    private List<MedUsage> findMedUsageByBatchId(Integer batchId) {
        BatchInStock batchInStock = batchInStockService.findBatchInStockById(batchId);
        List<MedUsage> medUsageListByBatchId = medUsageRepository.findByMedUsageId_BatchInStock(batchInStock);

        if (medUsageListByBatchId.isEmpty()) {
            String errorMessage = String.format("Данная партия медицинского препарата с id %d еще ни разу не была использована при проведении медицинской процедуры.", batchInStock.getMedication().getId());
            throw new CustomBackendException(errorMessage, HttpStatus.NOT_FOUND);
        }

        return  medUsageListByBatchId;
    }*/

    public MedUsageResponse customizeMedUsageResponse(MedUsage medUsage) {
        MedUsageResponse medUsageResponse = objectMapper.convertValue(medUsage, MedUsageResponse.class);

        BatchInStockResponse batchInStockResponse = batchInStockService.customizeBatchInStockResponse(medUsage.getMedUsageId().getBatchInStock());
        medUsageResponse.setBatchInStockId(batchInStockResponse.getId());
        medUsageResponse.setBatchInStockResponse(batchInStockResponse);

        MedProcedureResponse medProcedureResponse = medProcedureService.customizeMedProcedureResponse(medUsage.getMedUsageId().getMedProcedure());
        medUsageResponse.setMedProcedureResponse(medProcedureResponse);

        return medUsageResponse;
    }

    private Page<MedUsageResponse> composeMedUsageResponsePage(Pageable pageRequest, Page<MedUsage> medUsages) {
        List<MedUsageResponse> content = medUsages.getContent().stream()
                .map(this::customizeMedUsageResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, medUsages.getTotalElements());
    }

    public Page<MedUsageResponse> getAllMedUsages(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedUsage> medUsages = medUsageRepository.findAll(pageRequest);

        validatePageNotEmpty(medUsages, "На данный момент ни один препарат из партий на складе не расходовался.");

        return composeMedUsageResponsePage(pageRequest, medUsages);
    }

    public MedUsageResponse getMedUsageByMedProcedureAndBatchIds(Integer medProcedureId, Integer batchId) {
        return customizeMedUsageResponse(findMedUsageByMedUsageId(createMedUsageId(medProcedureId, batchId)));
    }

    public Page<MedUsageResponse> getAllMedUsagesByMedProcedureId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer medProcedureId) {
        MedProcedure medProcedure = medProcedureService.findMedProcedureById(medProcedureId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedUsage> medUsages = medUsageRepository.findAllByMedProcedure(pageRequest, medProcedure);

        validatePageNotEmpty(medUsages, "При проведении данной процедуры никакие медицинские препараты не использовались.");

        return composeMedUsageResponsePage(pageRequest, medUsages);
    }

    public Page<MedUsageResponse> getAllMedUsagesByBatchInStockId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer batchInStockId) {
        BatchInStock batchInStock = batchInStockService.findBatchInStockById(batchInStockId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedUsage> medUsages = medUsageRepository.findAllByBatchInStock(pageRequest, batchInStock);

        validatePageNotEmpty(medUsages, String.format(
                "Данная партия медицинского препарата с id %d еще ни разу не была использована при проведении медицинской процедуры.",
                batchInStock.getMedication().getId()
        ));

        return composeMedUsageResponsePage(pageRequest, medUsages);
    }

    public Page<MedUsageResponse> getAllMedUsagesByMedicationId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer medicationId) {
        Medication medication = medicationService.findMedicationById(medicationId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedUsage> medUsages = medUsageRepository.findAllByMedication(pageRequest, medication);

        validatePageNotEmpty(medUsages, "Данный препарат еще ни разу не был использован при проведении медицинской процедуры.");

        return composeMedUsageResponsePage(pageRequest, medUsages);
    }

    public MedUsageResponse addMedUsage(Integer medProcedureId, MedUsageRequest medUsageRequest) {
        checkIfVetInCharge(medProcedureId);
        checkAndAdjustStock(medUsageRequest.getDosageUsed(), medUsageRequest.getBatchInStockId());

        MedUsage medUsage = objectMapper.convertValue(medUsageRequest, MedUsage.class);
        medUsage.setMedUsageId(createMedUsageId(medProcedureId, medUsageRequest.getBatchInStockId()));

        return customizeMedUsageResponse(medUsageRepository.save(medUsage));
    }

    private void checkAndAdjustStock(Integer dosageUsed, Integer batchInStockId) {
        BatchInStock batchInStock = batchInStockService.findBatchInStockById(batchInStockId);

        if (!batchInStock.getMedication().getIsNowInUse()) {
            throw new CustomBackendException("Данный медицинский препарат в настоящее время не используется.", HttpStatus.BAD_REQUEST);
        }
        if (batchInStock.getExpirationDate().isBefore(LocalDate.now())) {
            throw new CustomBackendException("У данной партии медицинского препарата закончился срок годности", HttpStatus.BAD_REQUEST);
        }
        if (batchInStock.getTotalNumOfUnits() == 0) {
            throw new CustomBackendException("На складе закончилась данная партия медицинского препарата.", HttpStatus.BAD_REQUEST);
        }
        if (dosageUsed > batchInStock.getTotalNumOfUnits()) {
            throw new CustomBackendException("В данной партии не осталось достаточного количества медицинского препарата.", HttpStatus.BAD_REQUEST);
        }

        batchInStock.setTotalNumOfUnits(batchInStock.getTotalNumOfUnits() - dosageUsed);
        batchInStockService.updateBatchInStockTotalNumOfUnits(batchInStock);
    }

    private void rollbackStock(MedUsage medUsage) {
        BatchInStock batchInStock = batchInStockService.findBatchInStockById(medUsage.getMedUsageId().getBatchInStock().getId());
        batchInStock.setTotalNumOfUnits(batchInStock.getTotalNumOfUnits() + medUsage.getDosageUsed());
        batchInStockService.updateBatchInStockTotalNumOfUnits(batchInStock);
    }

    private void checkIfVetInCharge(Integer medProcedureId) {
        Employee vet = medProcedureService.findMedProcedureById(medProcedureId).getVet();
        Employee employee = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        if (!employee.equals(vet)) {
            throw new CustomBackendException("У вас недостаточно прав для осуществления учета медицинских препаратов, использованных во время данной процедуры", HttpStatus.FORBIDDEN);
        }
    }

    private MedUsageId createMedUsageId(Integer medProcedureId, Integer batchInStockId) {
        MedProcedure medProcedure = medProcedureService.findMedProcedureById(medProcedureId);
        BatchInStock batchInStock = batchInStockService.findBatchInStockById(batchInStockId);
        return new MedUsageId(medProcedure, batchInStock);
    }

    public MedUsageResponse updateMedUsage(Integer medProcedureId, Integer batchInStockId, MedUsageRequest medUsageRequest) {
        checkIfVetInCharge(medProcedureId);

        MedUsage medUsage = findMedUsageByMedUsageId(createMedUsageId(medProcedureId, batchInStockId));

        if (medUsageRequest.getDosageUsed() != null) {
            rollbackStock(medUsage);
            checkAndAdjustStock(medUsageRequest.getDosageUsed(), batchInStockId);
            medUsage.setDosageUsed(medUsageRequest.getDosageUsed());
        }

        return customizeMedUsageResponse(medUsageRepository.save(medUsage));
    }

    public void deleteMedUsage(Integer medProcedureId, Integer batchInStockId) {
        MedUsage medUsage = findMedUsageByMedUsageId(createMedUsageId(medProcedureId, batchInStockId));
        rollbackStock(medUsage);

        medUsageRepository.delete(medUsage);
    }
}

