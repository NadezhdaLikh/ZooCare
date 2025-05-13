package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.MedCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequest {
    @Schema(description = "Название медицинского препарата")
    private String name;

    @Schema(description = "Категория препарата")
    private MedCategory medCategory;

    @Schema(description = "Используется ли препарат в настоящее время")
    private Boolean isNowInUse;
}
