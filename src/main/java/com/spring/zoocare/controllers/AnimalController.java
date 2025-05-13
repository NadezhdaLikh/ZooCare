package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.AnimalRequest;
import com.spring.zoocare.models.dtos.responses.AnimalResponse;
import com.spring.zoocare.models.enums.HealthStatus;
import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Species;
import com.spring.zoocare.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/animals")
@RequiredArgsConstructor
@Tag(name = "Животные")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить животное по его id")
    public AnimalResponse getAnimalById(@PathVariable Integer id) {
        return animalService.getAnimal(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех животных в зоопарке (с фильтрацией по имени, подвиду или месту рождения)")
    public Page<AnimalResponse> getAllAnimalsFiltered(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @RequestParam(defaultValue = "id") String sortParam,
                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                      @RequestParam(required = false) String filter) {
        return animalService.getAllAnimalsFiltered(page, pageSize, sortParam, sortDirection, filter);
    }

    @GetMapping("/by-species")
    @Operation(summary = "Получить список всех животных указанного вида")
    public Page<AnimalResponse> getAllAnimalsBySpecies(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "id") String sortParam,
                                                       @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                       @RequestParam Species species) {
        return animalService.getAllAnimalsBySpecies(page, pageSize, sortParam, sortDirection, species);
    }

    @GetMapping("/by-enclosure/{enclosureId}")
    @Operation(summary = "Получить список всех животных, проживающих в местообитании с данным id")
    public Page<AnimalResponse> getAllAnimalsByEnclosure(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(defaultValue = "id") String sortParam,
                                                         @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                         @PathVariable Integer enclosureId) {
        return animalService.getAllAnimalsByEnclosureId(page, pageSize, sortParam, sortDirection, enclosureId);
    }

    @GetMapping("/by-sex")
    @Operation(summary = "Получить список всех животных указанного пола")
    public Page<AnimalResponse> getAllAnimalsBySex(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection, @RequestParam Sex sex) {
        return animalService.getAllAnimalsBySex(page, pageSize, sortParam, sortDirection, sex);
    }

    @GetMapping("/by-health-status")
    @Operation(summary = "Получить список всех животных с указанным состоянием здоровья")
    public Page<AnimalResponse> getAllAnimalsByHealthStatus(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortParam,
                                                            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                                            @RequestParam HealthStatus healthStatus) {
        return animalService.getAllAnimalsByHealthStatus(page, pageSize, sortParam, sortDirection, healthStatus);
    }

    @GetMapping("/by-has-chronic-condition")
    @Operation(summary = "Получить список всех животных, страдающих/не страдающих хроническим заболеванием")
    public Page<AnimalResponse> getAllAnimalsByHasChronicCondition(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection, @RequestParam Boolean hasChronicCondition) {
        return animalService.getAllAnimalsByHasChronicCondition(page, pageSize, sortParam, sortDirection, hasChronicCondition);
    }

    @GetMapping("/by-is-quarantined")
    @Operation(summary = "Получить список всех животных, находящихся/не находящихся на карантине в данный момент")
    public Page<AnimalResponse> getAllAnimalsByIsQuarantined(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortParam,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection, @RequestParam Boolean isQuarantined) {
        return animalService.getAllAnimalsByIsQuarantined(page, pageSize, sortParam, sortDirection, isQuarantined);
    }

    @GetMapping("/by-born-in-captivity")
    @Operation(summary = "Получить список всех животных, родившихся/не родившихся в неволе")
    public Page<AnimalResponse> getAllAnimalsByBornInCaptivity(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                                               @RequestParam(defaultValue = "id") String sortParam,
                                                               @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection, @RequestParam Boolean bornInCaptivity) {
        return animalService.getAllAnimalsByBornInCaptivity(page, pageSize, sortParam, sortDirection, bornInCaptivity);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "Добавить новое животное")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse addAnimal(@RequestBody @Valid AnimalRequest animalRequest) {
        return animalService.addAnimal(animalRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VET', 'ADMIN')")
    @Operation(summary = "Обновить животное по его id")
    public AnimalResponse updateAnimal(@PathVariable Integer id, @RequestBody @Valid AnimalRequest animalRequest) {
        return animalService.updateAnimal(id, animalRequest);
    }
}
