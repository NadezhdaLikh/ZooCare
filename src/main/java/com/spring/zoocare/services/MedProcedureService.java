package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.*;
import com.spring.zoocare.models.database.repositories.MedProcedureRepository;
import com.spring.zoocare.models.dtos.requests.MedProcedureRequest;
import com.spring.zoocare.models.dtos.responses.MedProcedureResponse;
import com.spring.zoocare.models.enums.ProcedureType;
import com.spring.zoocare.models.enums.Role;
import com.spring.zoocare.utils.PaginationUtils;
import com.spring.zoocare.utils.SecurityUtils;
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
public class MedProcedureService {

    private final ObjectMapper objectMapper;
    private final MedProcedureRepository medProcedureRepository;
    private final AnimalService animalService;
    private final EmployeeService employeeService;

    public MedProcedure findMedProcedureById(Integer id) {
        Optional<MedProcedure> optionalMedProcedure = medProcedureRepository.findById(id);

        return optionalMedProcedure.orElseThrow(() -> new CustomBackendException("Медицинская процедура с данным id не найдена.", HttpStatus.NOT_FOUND));
    }

    public MedProcedureResponse getMedProcedure(Integer id) {
        return customizeMedProcedureResponse(findMedProcedureById(id));
    }

    public Page<MedProcedureResponse> getAllMedProceduresFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by name or description
        Page<MedProcedure> medProcedures;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            medProcedures = medProcedureRepository.findAllFiltered(pageRequest, filter);
        } else medProcedures = medProcedureRepository.findAll(pageRequest);

        return composeMedProcedureResponsePage(pageRequest, medProcedures);
    }

    public MedProcedureResponse customizeMedProcedureResponse(MedProcedure medProcedure) {
        MedProcedureResponse medProcedureResponse = objectMapper.convertValue(medProcedure, MedProcedureResponse.class);
        medProcedureResponse.setAnimalId(medProcedure.getAnimal().getId());
        medProcedureResponse.setVetId(medProcedure.getVet().getId());

        return medProcedureResponse;
    }

    private Page<MedProcedureResponse> composeMedProcedureResponsePage(Pageable pageRequest, Page<MedProcedure> medProcedures) {
        List<MedProcedureResponse> content = medProcedures.getContent().stream()
                .map(this::customizeMedProcedureResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, medProcedures.getTotalElements());
    }

    public Page<MedProcedureResponse> getAllMedProceduresByAnimalId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer animalId) {
        Animal animal = animalService.findAnimalById(animalId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedProcedure> medProcedures = medProcedureRepository.findAllByAnimal(pageRequest, animal);

        return composeMedProcedureResponsePage(pageRequest, medProcedures);
    }

    public Page<MedProcedureResponse> getAllMedProceduresByVetId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer vetId) {
        Employee vet = employeeService.findEmployeeById(vetId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedProcedure> medProcedures = medProcedureRepository.findAllByVet(pageRequest, vet);

        return composeMedProcedureResponsePage(pageRequest, medProcedures);
    }

    public Page<MedProcedureResponse> getAllMedProceduresByProcedureType(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, ProcedureType procedureType) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<MedProcedure> medProcedures = medProcedureRepository.findAllByProcedureType(pageRequest, procedureType);

        return composeMedProcedureResponsePage(pageRequest, medProcedures);
    }

    public MedProcedureResponse addMedProcedure(MedProcedureRequest medProcedureRequest) {
        MedProcedure medProcedure = objectMapper.convertValue(medProcedureRequest, MedProcedure.class);

        Animal animal = animalService.findAnimalById(medProcedureRequest.getAnimalId());
        medProcedure.setAnimal(animal);

        Employee vet = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        medProcedure.setVet(vet);

        medProcedureRepository.save(medProcedure);

        return customizeMedProcedureResponse(medProcedure);
    }

    public MedProcedureResponse updateMedProcedure(Integer id, MedProcedureRequest medProcedureRequest) {
        MedProcedure medProcedure = findMedProcedureById(id);

        Employee employee1 = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        if(!employee1.equals(medProcedure.getVet())) {
            throw new CustomBackendException("У вас недостаточно прав для редактирования записи о данной медицинской процедуре.", HttpStatus.FORBIDDEN);
        }

        medProcedure.setAnimal(medProcedureRequest.getAnimalId() != null
                ? animalService.findAnimalById(medProcedureRequest.getAnimalId())
                : medProcedure.getAnimal());

        if (medProcedureRequest.getVetId() != null) {
            Employee employee2 = employeeService.findEmployeeById(medProcedureRequest.getVetId());

            if (!employee2.getRole().equals(Role.VET)) {
                throw new CustomBackendException("В записи об медицинской процедуре всегда обязательно должен быть указан id именно ветеринара, ответственного на эту процедуру.", HttpStatus.BAD_REQUEST);
            }

            medProcedure.setVet(employee2);
        } else medProcedure.setVet(medProcedure.getVet());

        medProcedure.setProcedureType(medProcedureRequest.getProcedureType() != null
                ? medProcedureRequest.getProcedureType()
                : medProcedure.getProcedureType());
        medProcedure.setName(medProcedureRequest.getName() != null
                ? medProcedureRequest.getName()
                : medProcedure.getName());
        medProcedure.setDescription(medProcedureRequest.getDescription() != null
                ? medProcedureRequest.getDescription()
                : medProcedure.getDescription());
        medProcedure.setPerformedOn(medProcedureRequest.getPerformedOn() != null
                ? medProcedureRequest.getPerformedOn()
                : medProcedure.getPerformedOn());
        medProcedure.setNotes(medProcedureRequest.getNotes() != null
                ? medProcedureRequest.getNotes()
                : medProcedure.getNotes());

        medProcedureRepository.save(medProcedure);

        return customizeMedProcedureResponse(medProcedure);
    }
}

