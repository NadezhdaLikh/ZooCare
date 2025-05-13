package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.ObservationVetRequest;
import com.spring.zoocare.models.dtos.requests.ObservationZookeeperRequest;
import com.spring.zoocare.models.dtos.responses.ObservationResponse;
import com.spring.zoocare.services.ObservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/zoocare-api/v1/observations")
@RequiredArgsConstructor
@Tag(name = "Наблюдения смотрителей зоопарка")
public class ObservationController {

    private final ObservationService observationService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить наблюдение по его id")
    public ObservationResponse getObservation(@PathVariable Integer id) {
        return observationService.getObservation(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех наблюдений (с фильтрацией по сообщению от смотрителя или по комментариям ветеринара")
    public Page<ObservationResponse> getAllObservationsFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "id") String sortParam,
                                                                @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                @RequestParam(required = false) String filter) {
        return observationService.getAllObservationsFiltered(page, pageSize, sortParam, sortDirect, filter);
    }

    @GetMapping("/by-animal/{animalId}")
    @Operation(summary = "Получить список всех наблюдений за животным по его id")
    public Page<ObservationResponse> getObservationsByAnimalId(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                                               @RequestParam(defaultValue = "id") String sortParam,
                                                               @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                               @PathVariable Integer animalId) {
        return observationService.getAllObservationsByAnimalId(page, pageSize, sortParam, sortDirect, animalId);
    }

    @GetMapping("/by-zookeeper/{zookeeperId}")
    @Operation(summary = "Получить список всех наблюдений по id смотрителя, который их сделал")
    public Page<ObservationResponse> getObservationsByZookeeperId(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortParam,
                                                                  @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                  @PathVariable Integer zookeeperId) {
        return observationService.getAllObservationsByZookeeperId(page, pageSize, sortParam, sortDirect, zookeeperId);
    }

    @GetMapping("/by-vet/{vetId}")
    @Operation(summary = "Получить список всех наблюдений по id ветеринара, который их заверил")
    public Page<ObservationResponse> getObservationsByVetId(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortParam,
                                                            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                            @PathVariable Integer vetId) {
        return observationService.getAllObservationsByVetId(page, pageSize, sortParam, sortDirect, vetId);
    }

    @GetMapping("/by-is-urgent")
    @Operation(summary = "Получить список всех наблюдений, которые являются/не являются срочными для заверения ветеринаром")
    public Page<ObservationResponse> getObservationsByVetId(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortParam,
                                                            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                            @RequestParam Boolean isUrgent) {
        return observationService.getAllObservationsByIsUrgent(page, pageSize, sortParam, sortDirect, isUrgent);
    }

    @GetMapping("/by-is-checked-by-vet")
    @Operation(summary = "Получить список всех наблюдений, которые уже были/еще не были заверены ветеринаром")
    public Page<ObservationResponse> getObservationsByIsCheckedByVet(@RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                                     @RequestParam(defaultValue = "id") String sortParam,
                                                                     @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                     @RequestParam Boolean isCheckedByVet) {
        return observationService.getAllObservationsByIsCheckedByVet(page, pageSize, sortParam, sortDirect, isCheckedByVet);
    }

    @GetMapping("/by-made-on")
    @Operation(summary = "Получить список всех наблюдений, сделанных в указанный день")
    public Page<ObservationResponse> getObservationsByMadeOn(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortParam,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                             @RequestParam LocalDate madeOn) {
        return observationService.getAllObservationsByMadeOn(page, pageSize, sortParam, sortDirect, madeOn);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ZOOKEEPER')")
    @Operation(summary = "Добавить новое наблюдение")
    public ObservationResponse addObservation(@RequestBody @Valid ObservationZookeeperRequest observationRequest) {
        return observationService.addObservation(observationRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ZOOKEEPER')")
    @Operation(summary = "Обновить наблюдение по его id")
    public ObservationResponse updateObservationByZookeeper(@PathVariable Integer id, @RequestBody @Valid ObservationZookeeperRequest observationRequest) {
        return observationService.updateObservationByZookeeper(id, observationRequest);
    }

    @PatchMapping("/{id}/vet-check")
    @PreAuthorize("hasAuthority('VET')")
    @Operation(summary = "Заверить наблюдение по его id")
    public ObservationResponse updateObservationByVet(@PathVariable Integer id, @RequestBody ObservationVetRequest observationVetRequest) {
        return observationService.updateObservationByVet(id, observationVetRequest);
    }
}
