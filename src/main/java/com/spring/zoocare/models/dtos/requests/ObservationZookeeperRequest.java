package com.spring.zoocare.models.dtos.requests;

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
public class ObservationZookeeperRequest {
    @Schema(description = "Id смотрителя зоопарка")
    // @NotNull(message = "Необходимо указать id смотрителя зоопарка.")
    Integer zookeeperId;

    @Schema(description = "Id животного")
    // @NotNull(message = "Необходимо указать id животного.")
    Integer animalId;

    @Schema(description = "Дата, когда было сделано наблюдение")
    LocalDate madeOn;

    @Schema(description = "Срочно ли ветеринар должен заверить запись о наблюдении")
    Boolean isUrgent;

    @Schema(description = "Комментарии смотрителя зоопарка")
    String message;
}
