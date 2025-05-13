package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.Animal;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.database.entities.Observation;
import com.spring.zoocare.models.database.repositories.ObservationRepository;
import com.spring.zoocare.models.dtos.requests.ObservationVetRequest;
import com.spring.zoocare.models.dtos.requests.ObservationZookeeperRequest;
import com.spring.zoocare.models.dtos.responses.ObservationResponse;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObservationService {

    private final ObjectMapper objectMapper;
    private final ObservationRepository observationRepository;
    private final AnimalService animalService;
    private final EmployeeService employeeService;

    public Observation findObservationById(Integer id) {
        Optional<Observation> optionalObservation = observationRepository.findById(id);

        return optionalObservation.orElseThrow(() -> new CustomBackendException("Запись о наблюдении с данным id  не найдена.", HttpStatus.NOT_FOUND));
    }

    public ObservationResponse getObservation(Integer id) {
        return customizeObservationResponse(findObservationById(id));
    }

    public Page<ObservationResponse> getAllObservationsFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by message or vet's feedback
        Page<Observation> observations;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            observations = observationRepository.findAllFiltered(pageRequest, filter);
        } else observations = observationRepository.findAll(pageRequest);

        return composeObservationResponsePage(pageRequest, observations);
    }

    private Page<ObservationResponse> composeObservationResponsePage(Pageable pageRequest, Page<Observation> observations) {
        List<ObservationResponse> content = observations.getContent().stream()
                .map(this::customizeObservationResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, observations.getTotalElements());
    }

    private ObservationResponse customizeObservationResponse(Observation observation) {
        ObservationResponse observationResponse = objectMapper.convertValue(observation, ObservationResponse.class);

        observationResponse.setAnimalId(observation.getAnimal().getId());
        observationResponse.setZookeeperId(observation.getZookeeper().getId());
        observationResponse.setVetId(observation.getVet() != null ? observation.getVet().getId() : null);

        return observationResponse;
    }

    public Page<ObservationResponse> getAllObservationsByAnimalId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer animalId) {
        Animal animal = animalService.findAnimalById(animalId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByAnimal(pageRequest, animal);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public Page<ObservationResponse> getAllObservationsByVetId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer vetId) {
        Employee vet = employeeService.findEmployeeById(vetId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByVet(pageRequest, vet);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public Page<ObservationResponse> getAllObservationsByZookeeperId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer zookeeperId) {
        Employee zookeeper = employeeService.findEmployeeById(zookeeperId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByZookeeper(pageRequest, zookeeper);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public Page<ObservationResponse> getAllObservationsByMadeOn(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, LocalDate madeOn) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByMadeOn(pageRequest, madeOn);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public Page<ObservationResponse> getAllObservationsByIsUrgent(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isUrgent) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByIsUrgent(pageRequest, isUrgent);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public Page<ObservationResponse> getAllObservationsByIsCheckedByVet(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isCheckedByVet) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Observation> observations = observationRepository.findAllByIsCheckedByVet(pageRequest, isCheckedByVet);

        return composeObservationResponsePage(pageRequest, observations);
    }

    public ObservationResponse addObservation(ObservationZookeeperRequest observationZookeeperRequest) {
        Observation observation = objectMapper.convertValue(observationZookeeperRequest, Observation.class);

        observation.setAnimal(animalService.findAnimalById(observationZookeeperRequest.getAnimalId()));
        observation.setZookeeper(employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail()));

        observationRepository.save(observation);

        return customizeObservationResponse(observation);
    }

    public ObservationResponse updateObservationByZookeeper(Integer id, ObservationZookeeperRequest observationZookeeperRequest) {
        Observation observation = findObservationById(id);

        Employee employee1 = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());

        if (!employee1.equals(observation.getZookeeper())) {
            throw new CustomBackendException("У вас недостаточно прав для редактирования записи о данном наблюдении.", HttpStatus.FORBIDDEN);
        }

        if (observationZookeeperRequest.getZookeeperId() != null) {
            Employee employee2 = employeeService.findEmployeeById(observationZookeeperRequest.getZookeeperId());

            if (!employee2.getRole().equals(Role.ZOOKEEPER)) {
                throw new CustomBackendException("В записи о наблюдении всегда обязательно должен быть указан id именно смотрителя зоопарка, ответственного за это наблюдение.", HttpStatus.BAD_REQUEST);
            }

            observation.setZookeeper(employee2);
        } else observation.setZookeeper(observation.getZookeeper());

        observation.setAnimal(observationZookeeperRequest.getAnimalId() != null
                ? animalService.findAnimalById(observationZookeeperRequest.getAnimalId())
                : observation.getAnimal());

        observation.setMadeOn(observationZookeeperRequest.getMadeOn() != null
                ? observationZookeeperRequest.getMadeOn()
                : observation.getMadeOn());

        observation.setIsUrgent(observationZookeeperRequest.getIsUrgent() != null
                ? observationZookeeperRequest.getIsUrgent()
                : observation.getIsUrgent());

        observation.setMessage(observationZookeeperRequest.getMessage() != null
                ? observationZookeeperRequest.getMessage()
                : observation.getMessage());

        observation.setIsCheckedByVet(false);
        observation.setVet(null);
        observationRepository.save(observation);

        return customizeObservationResponse(observation);
    }

    public ObservationResponse updateObservationByVet(Integer id, ObservationVetRequest observationVetRequest) {
        Employee employee = employeeService.findEmployeeByEmail(SecurityUtils.getAuthenticatedUserEmail());

        if (!employee.getRole().equals(Role.VET)) {
            throw new CustomBackendException("Только ветеринар может заверить запись о данном наблюдении.", HttpStatus.FORBIDDEN);
        }

        Observation observation = findObservationById(id);
        observation.setVet(employee);
        observation.setIsCheckedByVet(true);
        observation.setVetFeedback(observationVetRequest.getVetFeedback());

        observationRepository.save(observation);

        return customizeObservationResponse(observation);
    }
}
