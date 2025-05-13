package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.MedUsageRequest;
import com.spring.zoocare.models.enums.DosageUnits;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedUsageResponse extends MedUsageRequest {
    @Schema(description = "Сведения о медицинской процедуре")
    MedProcedureResponse medProcedureResponse;

    @Schema(description = "Сведения о партии на складе")
    BatchInStockResponse batchInStockResponse;
}
