package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.BatchInStock;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.entities.Enclosure;
import com.spring.zoocare.models.database.repositories.EnclosureRepository;
import com.spring.zoocare.models.dtos.requests.EnclosureRequest;
import com.spring.zoocare.models.dtos.responses.BatchInStockResponse;
import com.spring.zoocare.models.dtos.responses.EmployeeResponse;
import com.spring.zoocare.models.dtos.responses.EnclosureResponse;
import com.spring.zoocare.models.enums.EnclosureType;
import com.spring.zoocare.models.enums.ZooZone;
import com.spring.zoocare.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnclosureService {

    private final ObjectMapper objectMapper;
    private final EnclosureRepository enclosureRepository;

    public Enclosure findEnclosureById(Integer id) {
        Optional<Enclosure> optionalEnclosure = enclosureRepository.findById(id);

        return optionalEnclosure.orElseThrow(() -> new CustomBackendException("Местообитание с данным id не найдено.", HttpStatus.NOT_FOUND));
    }

    public EnclosureResponse getEnclosure(Integer id) {
        return objectMapper.convertValue(findEnclosureById(id), EnclosureResponse.class);
    }

    public Page<EnclosureResponse> getAllEnclosures(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Enclosure> enclosures = enclosureRepository.findAll(pageRequest);

        return composeEnclosureResponsePage(pageRequest, enclosures);
    }

    private Page<EnclosureResponse> composeEnclosureResponsePage(Pageable pageRequest, Page<Enclosure> enclosures) {
        List<EnclosureResponse> content = enclosures.getContent().stream()
                .map(e -> objectMapper.convertValue(e, EnclosureResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, enclosures.getTotalElements());
    }

    public Page<EnclosureResponse> getAllEnclosuresByZooZone(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, ZooZone zooZone) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Enclosure> enclosures = enclosureRepository.findAllByZooZone(pageRequest, zooZone);

        return composeEnclosureResponsePage(pageRequest, enclosures);
    }

    public Page<EnclosureResponse> getAllEnclosuresByEnclosureType(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, EnclosureType enclosureType) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Enclosure> enclosures = enclosureRepository.findAllByEnclosureType(pageRequest, enclosureType);

        return composeEnclosureResponsePage(pageRequest, enclosures);
    }

    public Page<EnclosureResponse> getAllEnclosuresByIsNowInhabitable(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isNowInhabitable) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Enclosure> enclosures = enclosureRepository.findAllByIsNowInhabitable(pageRequest, isNowInhabitable);

        return composeEnclosureResponsePage(pageRequest, enclosures);
    }

    public EnclosureResponse addEclosure(EnclosureRequest enclosureRequest) {
        checkCapacity(enclosureRequest.getCapacity());

        Enclosure enclosure = objectMapper.convertValue(enclosureRequest, Enclosure.class);

        return objectMapper.convertValue(enclosureRepository.save(enclosure), EnclosureResponse.class);
    }

    public EnclosureResponse updateEnclosure(Integer id, EnclosureRequest enclosureRequest) {
        checkCapacity(enclosureRequest.getCapacity());

        Enclosure enclosure = findEnclosureById(id);

        enclosure.setZooZone(enclosureRequest.getZooZone() != null ? enclosureRequest.getZooZone() : enclosure.getZooZone());
        enclosure.setEnclosureType(enclosureRequest.getEnclosureType() != null ? enclosureRequest.getEnclosureType() : enclosure.getEnclosureType());
        enclosure.setCapacity(enclosureRequest.getCapacity() != null ? enclosureRequest.getCapacity() : enclosure.getCapacity());

        if (enclosureRequest.getIsNowInhabitable() != null) {
            if (enclosureRequest.getIsNowInhabitable().equals(enclosure.getIsNowInhabitable())) {
                enclosure.setIsNowInhabitable(enclosure.getIsNowInhabitable());
            } else if (!enclosureRequest.getIsNowInhabitable()) {
                declareEnclosureInhabitable(enclosure.getId());
            } else enclosure.setIsNowInhabitable(true);
        }

        return objectMapper.convertValue(enclosureRepository.save(enclosure), EnclosureResponse.class);
    }

    private void checkCapacity(Integer capacity) {
        if (capacity < 0) {
            throw new CustomBackendException("Вместимость местообитания не может быть меньше нуля.", HttpStatus.BAD_REQUEST);
        }
    }

    public void updateEnclosureAnimals(Enclosure enclosure) {
        enclosureRepository.save(enclosure);
    }

    public void declareEnclosureInhabitable(Integer id) {
        Enclosure enclosure = findEnclosureById(id);

        if (!enclosure.getAnimals().isEmpty()) {
            throw new CustomBackendException("Перед тем как сделать данное местообитание недоступным, необходимо переселить проживающих в нем животных в другое местообитание.",
                    HttpStatus.BAD_REQUEST);
        }
        enclosure.setIsNowInhabitable(false);

        enclosureRepository.save(enclosure);
    }
}
