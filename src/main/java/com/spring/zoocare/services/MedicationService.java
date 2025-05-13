package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.Medication;
import com.spring.zoocare.models.database.repositories.MedicationRepository;
import com.spring.zoocare.models.dtos.requests.MedicationRequest;
import com.spring.zoocare.models.dtos.responses.MedicationResponse;
import com.spring.zoocare.models.enums.MedCategory;
import com.spring.zoocare.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final ObjectMapper objectMapper;
    private final MedicationRepository medicationRepository;

    public Medication findMedicationById(Integer id) {
        Optional<Medication> optionalMedication = medicationRepository.findById(id);

        return optionalMedication.orElseThrow(() -> new CustomBackendException("Медицинский препарат с данным id не найден.", HttpStatus.NOT_FOUND));
    }

    public MedicationResponse getMedication(Integer id) {
        return objectMapper.convertValue(findMedicationById(id), MedicationResponse.class);
    }

    public Page<MedicationResponse> getAllMedicationsFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by name
        Page<Medication> medications;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            medications = medicationRepository.findAllFiltered(pageRequest, filter);
        } else medications = medicationRepository.findAll(pageRequest);

        return composeMedicationResponsePage(pageRequest, medications);
    }

    private Page<MedicationResponse> composeMedicationResponsePage(Pageable pageRequest, Page<Medication> medications) {
        List<MedicationResponse> content = medications.getContent().stream()
                .map(m -> objectMapper.convertValue(m, MedicationResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, medications.getTotalElements());
    }

    public Page<MedicationResponse> getAllMedicationsByMedCategory(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, MedCategory medCategory) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Medication> medications = medicationRepository.findAllByMedCategory(pageRequest, medCategory);

        return composeMedicationResponsePage(pageRequest, medications);
    }

    public Page<MedicationResponse> getAllMedicationsByIsNowInUse(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isNowInUse) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Medication> medications = medicationRepository.findAllByIsNowInUse(pageRequest, isNowInUse);

        return composeMedicationResponsePage(pageRequest, medications);
    }

    public MedicationResponse addMedication(MedicationRequest medicationRequest) {
        Medication medication = objectMapper.convertValue(medicationRequest, Medication.class);

        return objectMapper.convertValue(medicationRepository.save(medication), MedicationResponse.class);
    }

    public MedicationResponse updateMedication(Integer id, MedicationRequest medicationRequest) {
        Medication medication = findMedicationById(id);

        if (medicationRequest.getName() != null) {
            medication.setName(medicationRequest.getName());
        }
        if (medicationRequest.getMedCategory() != null) {
            medication.setMedCategory(medicationRequest.getMedCategory());
        }
        if (medicationRequest.getIsNowInUse() != null) {
            medication.setIsNowInUse(medicationRequest.getIsNowInUse());
        }

        return objectMapper.convertValue(medicationRepository.save(medication), MedicationResponse.class);
    }

    public MedicationResponse declareMedicationObsolete(Integer id) {
        Medication medication = findMedicationById(id);
        medication.setIsNowInUse(false);

        return objectMapper.convertValue(medicationRepository.save(medication), MedicationResponse.class);
    }
}
