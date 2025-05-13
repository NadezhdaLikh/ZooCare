package com.spring.zoocare.controllers;

import com.spring.zoocare.models.dtos.requests.EnclosureRequest;
import com.spring.zoocare.models.dtos.responses.EnclosureResponse;
import com.spring.zoocare.models.enums.EnclosureType;
import com.spring.zoocare.models.enums.ZooZone;
import com.spring.zoocare.services.EnclosureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zoocare-api/v1/enclosures")
@RequiredArgsConstructor
@Tag(name = "Местообитания зоопарка")
public class EnclosureController {

    private final EnclosureService enclosureService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить местообитание по его id")
    public EnclosureResponse getEnclosure(@PathVariable Integer id) {
        return enclosureService.getEnclosure(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех местообитаний")
    public Page<EnclosureResponse> getAllEnclosures(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortParam,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect) {
        return enclosureService.getAllEnclosures(page, pageSize, sortParam, sortDirect);
    }

    @GetMapping("/by-zoo-zone")
    @Operation(summary = "Получить список всех местообитаний, находящихся в указанной зоне")
    public Page<EnclosureResponse> getAllEnclosuresByZooZone(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortParam,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                             @RequestParam ZooZone zooZone) {
        return enclosureService.getAllEnclosuresByZooZone(page, pageSize, sortParam, sortDirect, zooZone);
    }

    @GetMapping("/by-type")
    @Operation(summary = "Получить список всех местообитаний указанного типа")
    public Page<EnclosureResponse> getAllEnclosuresByEnclosureType(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortParam,
                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                   @RequestParam EnclosureType enclosureType) {
        return enclosureService.getAllEnclosuresByEnclosureType(page, pageSize, sortParam, sortDirect, enclosureType);
    }

    @GetMapping("/currently-inhabitable")
    @Operation(summary = "Получить список всех местообитаний, которые на данный момент пригодны/не пригодны для заселения животных")
    public Page<EnclosureResponse> getAllEnclosuresByIsNowInhabitable(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "id") String sortParam,
                                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sortDirect,
                                                                      @RequestParam Boolean isNowInhabitable) {
        return enclosureService.getAllEnclosuresByIsNowInhabitable(page, pageSize, sortParam, sortDirect, isNowInhabitable);
    }

    @PostMapping
    @Operation(summary = "Добавить новое местообитание")
    public EnclosureResponse addEnclosure(@RequestBody @Valid EnclosureRequest enclosureRequest) {
        return enclosureService.addEclosure(enclosureRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить местообитание по его id")
    public EnclosureResponse updateEnclosure(@PathVariable Integer id, @RequestBody @Valid EnclosureRequest enclosureRequest) {
        return enclosureService.updateEnclosure(id, enclosureRequest);
    }

    @PatchMapping("/{id}/declare-inhabitable")
    @Operation(summary = "По id местообитания обозначить его непригодным для заселения в настоящее время")
    public void declareEnclosureInhabitable(@PathVariable Integer id) {
        enclosureService.declareEnclosureInhabitable(id);
    }
}