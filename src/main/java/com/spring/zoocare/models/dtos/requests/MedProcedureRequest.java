package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.ProcedureType;
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
public class MedProcedureRequest {
    @Schema(description = "Id животного")
    @NotNull(message = "Необходимо указать id животного.")
    Integer animalId;

    @Schema(description = "Id ветеринара")
    // @NotNull(message = "Необходимо указать id ветеринара.")
    Integer vetId;

    @Schema(description = "Тип медицинской процедуры")
    ProcedureType procedureType;

    @Schema(description = "Название процедуры")
    String name;

    @Schema(description = "Краткое писание процедуры")
    String description;

    @Schema(description = "Дата проведения процедуры")
    LocalDate performedOn;

    @Schema(description = "Комментарии")
    String notes;
}
