package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.CauseOfDeath;
import com.spring.zoocare.models.enums.HealthStatus;
import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Species;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalRequest {
    @Schema(description = "Вид животного")
    Species species;

    @Schema(description = "Подвид животного")
    String subspecies;

    @Schema(description = "Имя животного")
    String name;

    @Schema(description = "Пол животного")
    Sex sex;

    @Schema(description = "Дата рождения животного")
    LocalDate birthDate;

    @Schema(description = "Родилось ли животное в неволе")
    Boolean bornInCaptivity;

    @Schema(description = "Место рождения животного")
    String placeOfBirth;

    @Schema(description = "Id местообитания, в котором проживает животное")
    @NotNull(message = "Необходимо указать id местообитания.")
    Integer enclosureId;

    @Schema(description = "Вес животного")
    Float weight;

    @Schema(description = "Рост животного")
    Float height;

    @Schema(description = "Состояние здоровья животного")
    HealthStatus healthStatus;

    @Schema(description = "Есть ли у животного какое-либо хроническое заболевание")
    Boolean hasChronicCondition;

    @Schema(description = "Находится ли животное на карантине")
    Boolean isQuarantined;

    @Schema(description = "Комментарии")
    String notes;
}