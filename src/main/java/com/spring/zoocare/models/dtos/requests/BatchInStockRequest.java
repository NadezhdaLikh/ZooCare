package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.models.enums.DosageForm;
import com.spring.zoocare.models.enums.DosageUnits;
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
public class BatchInStockRequest {
    @Schema(description = "Id медицинского препарата")
    @NotNull(message = "Необходимо указать id медицинского препарата.")
    Integer medicationId;

    @Schema(description = "Id поставщика")
    @NotNull(message = "Необходимо указать id поставщика медицинского препарата.")
    Integer medSupplierId;

    @Schema(description = "Лекарственная форма")
    DosageForm dosageForm;

    @Schema(description = "Единицы дозировки")
    DosageUnits dosageUnits;

    @Schema(description = "Количество единиц дозировки в наличии")
    Integer totalNumOfUnits;

    @Schema(description = "Способ введения лекарственного препарата")
    AdministrationRoute administrationRoute;

    @Schema(description = "Дата приема поставки")
    LocalDate deliveredOn;

    @Schema(description = "Место отправки поставки")
    String deliveredFrom;

    @Schema(description = "Дата истечения срока годности")
    LocalDate expirationDate;
}
