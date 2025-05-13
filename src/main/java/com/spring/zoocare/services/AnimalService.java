package com.spring.zoocare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.Animal;
import com.spring.zoocare.models.database.entities.Enclosure;
import com.spring.zoocare.models.database.repositories.AnimalRepository;
import com.spring.zoocare.models.dtos.requests.AnimalRequest;
import com.spring.zoocare.models.dtos.responses.AnimalResponse;
import com.spring.zoocare.models.dtos.responses.EnclosureResponse;
import com.spring.zoocare.models.enums.HealthStatus;
import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Species;
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
public class AnimalService {

    private final ObjectMapper objectMapper;
    private final AnimalRepository animalRepository;
    private final EnclosureService enclosureService;

    public Animal findAnimalById(Integer id) {
        Optional<Animal> optionalAnimal = animalRepository.findById(id);

        return optionalAnimal.orElseThrow(() -> new CustomBackendException("Животное с данным id  не найдено.", HttpStatus.NOT_FOUND));
    }

    public AnimalResponse getAnimal(Integer id) {
        return customizeAnimalResponse(findAnimalById(id));
    }

    public Page<AnimalResponse> getAllAnimalsFiltered(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, String filter) { // Filtered by name, subspecies or place of birth
        Page<Animal> animals;
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);

        if (StringUtils.hasText(filter)) {
            animals = animalRepository.findAllFiltered(pageRequest, filter);
        } else animals = animalRepository.findAll(pageRequest);

        return composeAnimalResponsePage(pageRequest, animals);
    }
    // GET /zoocare/animals/subspecies?page=0&pageSize=10&sortParam=name&sortDirection=ASC&subspecies=Пантера

    public AnimalResponse customizeAnimalResponse(Animal animal) {
        AnimalResponse animalResponse = objectMapper.convertValue(animal, AnimalResponse.class);

        EnclosureResponse enclosureResponse = objectMapper.convertValue(animal.getEnclosure(), EnclosureResponse.class);
        animalResponse.setEnclosureId(enclosureResponse.getId());
        animalResponse.setEnclosureResponse(enclosureResponse);

        return animalResponse;
    }

    private Page<AnimalResponse> composeAnimalResponsePage(Pageable pageRequest, Page<Animal> animals) {
        List<AnimalResponse> content = animals.getContent().stream()
                .map(this::customizeAnimalResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, animals.getTotalElements());
    }

    public Page<AnimalResponse> getAllAnimalsBySpecies(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Species species) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllBySpecies(pageRequest, species);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public Page<AnimalResponse> getAllAnimalsByEnclosureId(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Integer enclosureId) {
        Enclosure enclosure = enclosureService.findEnclosureById(enclosureId);

        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllByEnclosure(pageRequest, enclosure);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public Page<AnimalResponse> getAllAnimalsBySex(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Sex sex) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllBySex(pageRequest, sex);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public Page<AnimalResponse> getAllAnimalsByBornInCaptivity(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean bornInCaptivity) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllByBornInCaptivity(pageRequest, bornInCaptivity);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public Page<AnimalResponse> getAllAnimalsByHealthStatus(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, HealthStatus healthStatus) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllByHealthStatus(pageRequest, healthStatus);

        return composeAnimalResponsePage(pageRequest, animals);
    }
    // GET /zoocare/animals/health-status?page=0&pageSize=5&sortParam=healthStatus&sortDirection=DESC&healthStatus=HEALTHY

    public Page<AnimalResponse> getAllAnimalsByHasChronicCondition(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean hasChronicCondition) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllByHasChronicCondition(pageRequest, hasChronicCondition);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public Page<AnimalResponse> getAllAnimalsByIsQuarantined(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect, Boolean isQuarantined) {
        Pageable pageRequest = PaginationUtils.makePageRequest(page, pageSize, sortParam, sortDirect);
        Page<Animal> animals = animalRepository.findAllByIsQuarantined(pageRequest, isQuarantined);

        return composeAnimalResponsePage(pageRequest, animals);
    }

    public AnimalResponse addAnimal(AnimalRequest animalRequest) {
        if (animalRequest.getEnclosureId() == null) {
            throw new CustomBackendException("При добавлении записи о новом животном обязательно указывать id местообитания, в котором оно будет жить.", HttpStatus.BAD_REQUEST);
        }

        Animal animal = objectMapper.convertValue(animalRequest, Animal.class);
        Enclosure enclosure = enclosureService.findEnclosureById(animalRequest.getEnclosureId());
        putAnimalInEnclosure(animal, enclosure);

        return customizeAnimalResponse(animal);
    }

    private void putAnimalInEnclosure(Animal animal, Enclosure enclosure) {
        if (!enclosure.getIsNowInhabitable()) {
            throw new CustomBackendException("Данное местообитание сейчас непригодно для заселения.", HttpStatus.BAD_REQUEST); // Enum EnclosureStatus with CLOSED, UNDER_CONSTRUCTION, UNDER_MAINTENANCE, ...
        }

        if (enclosure.getAnimals().size() == enclosure.getCapacity()) {
            throw new CustomBackendException("Данное местообитание уже заполнено.", HttpStatus.BAD_REQUEST);
        }

        enclosure.getAnimals().add(animal);
        animal.setEnclosure(enclosure);

        animalRepository.save(animal);
        enclosureService.updateEnclosureAnimals(enclosure);
    }

    public void relocateAnimal(Animal animal, Integer newEnclosureId) {
        Enclosure oldEnclosure = animal.getEnclosure();
        Enclosure newEnclosure = enclosureService.findEnclosureById(newEnclosureId);

        if (oldEnclosure.equals(newEnclosure)) {
            throw new CustomBackendException("Данное животное уже проживает в данном местообитании.", HttpStatus.CONFLICT);
        }

        oldEnclosure.getAnimals().remove(animal);
        enclosureService.updateEnclosureAnimals(oldEnclosure);

        putAnimalInEnclosure(animal, newEnclosure);
    }

    public AnimalResponse updateAnimal(Integer id, AnimalRequest animalRequest) {
        Animal animal = findAnimalById(id);

        animal.setSpecies(animalRequest.getSpecies() != null ? animalRequest.getSpecies() : animal.getSpecies());
        animal.setSubspecies(animalRequest.getSubspecies() != null ? animalRequest.getSubspecies() : animal.getSubspecies());
        animal.setName(animalRequest.getName() != null ? animalRequest.getName() : animal.getName());
        animal.setSex(animalRequest.getSex() != null ? animalRequest.getSex() : animal.getSex());
        animal.setBirthDate(animalRequest.getBirthDate() != null ? animalRequest.getBirthDate() : animal.getBirthDate());
        animal.setBornInCaptivity(animalRequest.getBornInCaptivity() != null ? animalRequest.getBornInCaptivity() : animal.getBornInCaptivity());
        animal.setPlaceOfBirth(animalRequest.getPlaceOfBirth() != null ? animalRequest.getPlaceOfBirth() : animal.getPlaceOfBirth());

        if (animalRequest.getEnclosureId() != null) {
            relocateAnimal(animal, animalRequest.getEnclosureId());
        } else animal.setEnclosure(animal.getEnclosure());

        animal.setWeight(animalRequest.getWeight() != null ? animalRequest.getWeight() : animal.getWeight());
        animal.setHeight(animalRequest.getHeight() != null ? animalRequest.getHeight() : animal.getHeight());
        animal.setHealthStatus(animalRequest.getHealthStatus() != null ? animalRequest.getHealthStatus() : animal.getHealthStatus());
        animal.setHasChronicCondition(animalRequest.getHasChronicCondition() != null ? animalRequest.getHasChronicCondition() : animal.getHasChronicCondition());
        animal.setIsQuarantined(animalRequest.getIsQuarantined() != null ? animalRequest.getIsQuarantined() : animal.getIsQuarantined());
        animal.setNotes(animalRequest.getNotes() != null ? animalRequest.getNotes() : animal.getNotes());

        return customizeAnimalResponse(animalRepository.save(animal));
    }
}
