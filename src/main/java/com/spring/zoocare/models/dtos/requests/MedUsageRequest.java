package com.spring.zoocare.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedUsageRequest {
    @Schema(description = "Id партии на складе")
    @NotNull(message = "Необходимо указать id партии со склада.")
    Integer batchInStockId;

    @Schema(description = "Использованная дозировка")
    Integer dosageUsed;
}
