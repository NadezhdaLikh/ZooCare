package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.MedProcedure;
import com.spring.zoocare.models.database.entities.MedSupplier;
import com.spring.zoocare.models.database.entities.Medication;
import com.spring.zoocare.models.database.repositories.MedSupplierRepository;
import com.spring.zoocare.models.dtos.requests.MedSupplierRequest;
import com.spring.zoocare.models.dtos.responses.MedProcedureResponse;
import com.spring.zoocare.models.dtos.responses.MedSupplierResponse;
import com.spring.zoocare.models.dtos.responses.MedicationResponse;
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
public class MedSupplierService {

    private final ObjectMapper objectMapper;
    private final MedSupplierRepository medSupplierRepository;

    public MedSupplier findMedSupplierById(Integer id) {
        Optional<MedSupplier> optionalMedSupplier = medSupplierRepository.findById(id);

        return optionalMedSupplier.orElseThrow(() -> new CustomBackendException("Поставщик с данным id не найден.", HttpStatus.NOT_FOUND));
    }

    public MedSupplierResponse getMedSupplier(Integer id) {
        return objectMapper.convertValue(findMedSupplierById(id), MedSupplierResponse.class);
    }

    public Page<MedSupplierResponse> getAllMedSuppliersFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by name or email
        Page<MedSupplier> medSuppliers;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            medSuppliers = medSupplierRepository.findAllFiltered(pageRequest, filter);
        } else medSuppliers = medSupplierRepository.findAll(pageRequest);

        return composeMedSupplierResponsePage(pageRequest, medSuppliers);
    }

    private Page<MedSupplierResponse> composeMedSupplierResponsePage(Pageable pageRequest, Page<MedSupplier> medSuppliers) {
        List<MedSupplierResponse> content = medSuppliers.getContent().stream()
                .map(m -> objectMapper.convertValue(m, MedSupplierResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, medSuppliers.getTotalElements());
    }

    public Page<MedSupplierResponse> getAllMedSuppliersIsNowSupplying(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isNowSupplying) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedSupplier> medSuppliers = medSupplierRepository.findAllByIsNowSupplying(pageRequest, isNowSupplying);

        return composeMedSupplierResponsePage(pageRequest, medSuppliers);
    }

    public MedSupplierResponse addMedSupplier(MedSupplierRequest medSupplierRequest) {
        medSupplierRepository.findByOgrn(medSupplierRequest.getOgrn()).ifPresent(m -> {
            throw new CustomBackendException("Поставщик с данным ОГРН уже существует.", HttpStatus.CONFLICT);
        });

        medSupplierRepository.findByInn(medSupplierRequest.getInn()).ifPresent(m -> {
            throw new CustomBackendException("Поставщик с данным ИНН уже существует.", HttpStatus.CONFLICT);
        });

        medSupplierRepository.findByKpp(medSupplierRequest.getKpp()).ifPresent(m -> {
            throw new CustomBackendException("Поставщик с данным КПП уже существует.", HttpStatus.CONFLICT);
        });

        MedSupplier medSupplier = objectMapper.convertValue(medSupplierRequest, MedSupplier.class);

        return objectMapper.convertValue(medSupplierRepository.save(medSupplier), MedSupplierResponse.class);
    }

    public MedSupplierResponse updateMedSupplier(Integer id, MedSupplierRequest medSupplierRequest) {
        MedSupplier medSupplier = findMedSupplierById(id);

        medSupplier.setName(medSupplierRequest.getName() != null ? medSupplierRequest.getName() : medSupplier.getName());
        medSupplier.setOgrn(medSupplierRequest.getOgrn() != null ? medSupplierRequest.getOgrn() : medSupplier.getOgrn());
        medSupplier.setInn(medSupplierRequest.getInn() != null ? medSupplierRequest.getInn() : medSupplier.getInn());
        medSupplier.setKpp(medSupplierRequest.getKpp() != null ? medSupplierRequest.getKpp() : medSupplier.getKpp());
        medSupplier.setEmail(medSupplierRequest.getEmail() != null ? medSupplierRequest.getEmail() : medSupplier.getEmail());
        medSupplier.setPhoneNumber(medSupplierRequest.getPhoneNumber() != null ? medSupplierRequest.getPhoneNumber() : medSupplier.getPhoneNumber());
        medSupplier.setLegalAddress(medSupplierRequest.getLegalAddress() != null ? medSupplierRequest.getLegalAddress() : medSupplier.getLegalAddress());
        medSupplier.setIsNowSupplying(medSupplierRequest.getIsNowSupplying() != null ? medSupplierRequest.getIsNowSupplying() : medSupplier.getIsNowSupplying());

        return objectMapper.convertValue(medSupplierRepository.save(medSupplier), MedSupplierResponse.class);
    }

    public MedSupplierResponse declareMedSupplierObsolete(Integer id) {
        MedSupplier medSupplier = findMedSupplierById(id);
        medSupplier.setIsNowSupplying(false);

        return objectMapper.convertValue(medSupplierRepository.save(medSupplier), MedSupplierResponse.class);
    }
}
