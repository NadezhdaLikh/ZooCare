package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.BatchInStockRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BatchInStockResponse extends BatchInStockRequest {
    @Schema(description = "Id партии на складе")
    private Integer id;

    @Schema(description = "Сведения о медицинском препарате, к которому принадлежит партия")
    private MedicationResponse medicationResponse;
}
