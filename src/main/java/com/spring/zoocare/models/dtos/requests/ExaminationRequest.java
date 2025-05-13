package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.ExaminationType;
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
public class ExaminationRequest {
    @Schema(description = "Id животного")
    @NotNull(message = "Необходимо указать id животного.")
    Integer animalId;

    @Schema(description = "Id ветеринара")
    // @NotNull(message = "Необходимо указать id ветеринара.")
    Integer vetId;

    @Schema(description = "Тип осмотра")
    ExaminationType examinationType;

    @Schema(description = "Дата проведения осмотра")
    LocalDate performedOn;

    @Schema(description = "Краткое описание")
    String description;

    @Schema(description = "Диагноз")
    String diagnosis;

    @Schema(description = "Рецепт")
    String prescription;
}
