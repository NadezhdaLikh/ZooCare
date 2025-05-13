package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.Animal;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.entities.Examination;
import com.spring.zoocare.models.database.repositories.ExaminationRepository;
import com.spring.zoocare.models.dtos.requests.ExaminationRequest;
import com.spring.zoocare.models.dtos.responses.ExaminationResponse;
import com.spring.zoocare.models.enums.ExaminationType;
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
public class ExaminationService {

    private final ObjectMapper objectMapper;
    private final ExaminationRepository examinationRepository;
    private final AnimalService animalService;
    private final EmployeeService employeeService;

    public Examination findExaminationById(Integer id) {
        Optional<Examination> optionalExamination = examinationRepository.findById(id);

        return optionalExamination.orElseThrow(() -> new CustomBackendException("Запись о медицинском осмотре с данным id не найдена.", HttpStatus.NOT_FOUND));
    }

    public ExaminationResponse getExamination(Integer id) {
        return customizeExaminationResponse(findExaminationById(id));
    }

    private Page<ExaminationResponse> composeExamiantionResponsePage(Pageable pageRequest, Page<Examination> examinations) {
        List<ExaminationResponse> content = examinations.getContent().stream()
                .map(this::customizeExaminationResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, examinations.getTotalElements());
    }

    public Page<ExaminationResponse> getAllExaminationsFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by diagnosis or prescription
        Page<Examination> examinations;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            examinations = examinationRepository.findAllFiltered(pageRequest, filter);
        } else examinations = examinationRepository.findAll(pageRequest);

        return composeExamiantionResponsePage(pageRequest, examinations);
    }

    public Page<ExaminationResponse> getAllExaminationsByAnimalId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer animalId) {
        Animal animal = animalService.findAnimalById(animalId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Examination> examinations = examinationRepository.findAllByAnimal(pageRequest, animal);

        return composeExamiantionResponsePage(pageRequest, examinations);
    }

    public Page<ExaminationResponse> getAllExaminationsByVetId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer vetId) {
        Employee vet = employeeService.findEmployeeById(vetId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Examination> examinations = examinationRepository.findAllByVet(pageRequest, vet);

        return composeExamiantionResponsePage(pageRequest, examinations);
    }

    public Page<ExaminationResponse> getAllExaminationsByExaminationType(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, ExaminationType examinationType) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Examination> examinations = examinationRepository.findAllByExaminationType(pageRequest, examinationType);

        return composeExamiantionResponsePage(pageRequest, examinations);
    }

    public ExaminationResponse addExamination(ExaminationRequest examinationRequest) {
        Examination examination = objectMapper.convertValue(examinationRequest, Examination.class);

        Animal animal = animalService.findAnimalById(examinationRequest.getAnimalId());
        examination.setAnimal(animal);

        Employee vet = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());
        examination.setVet(vet);

        examinationRepository.save(examination);

        return customizeExaminationResponse((examination));
    }

    public ExaminationResponse updateExamination(Integer id, ExaminationRequest examinationRequest) {
        Examination examination = findExaminationById(id);
        Employee employee1 = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());

        if (!employee1.equals(examination.getVet())) {
            throw new CustomBackendException("У вас недостаточно прав для редактирования записи о данном медицинском осмотре.", HttpStatus.FORBIDDEN);
        }

        examination.setAnimal(examinationRequest.getAnimalId() != null ? animalService.findAnimalById(examinationRequest.getAnimalId()) : examination.getAnimal());

        if (examinationRequest.getVetId() != null) {
            Employee employee2 = employeeService.findEmployeeById(examinationRequest.getVetId());

            if (!employee2.getRole().equals(Role.VET)) {
                throw new CustomBackendException("В записи об осмотре всегда обязательно должен быть указан id именно ветеринара, проводившего этот осмотр.", HttpStatus.BAD_REQUEST);
            }

            examination.setVet(employee2);
        } else examination.setVet(examination.getVet());

        examination.setExaminationType(examinationRequest.getExaminationType() != null ? examinationRequest.getExaminationType() : examination.getExaminationType());
        examination.setPerformedOn(examinationRequest.getPerformedOn() != null ? examinationRequest.getPerformedOn() : examination.getPerformedOn());
        examination.setDescription(examinationRequest.getDescription() != null ? examinationRequest.getDescription() : examination.getDescription());
        examination.setDiagnosis(examinationRequest.getDiagnosis() != null ? examinationRequest.getDiagnosis() : examination.getDiagnosis());
        examination.setPrescription(examinationRequest.getPrescription() != null ? examinationRequest.getPrescription() : examination.getPrescription());

        examinationRepository.save(examination);

        return customizeExaminationResponse(examination);
    }

    private ExaminationResponse customizeExaminationResponse(Examination examination) {
        ExaminationResponse examinationResponse = objectMapper.convertValue(examination, ExaminationResponse.class);
        examinationResponse.setAnimalId(examination.getAnimal().getId());
        examinationResponse.setVetId(examination.getVet().getId());

        return examinationResponse;
    }
}
