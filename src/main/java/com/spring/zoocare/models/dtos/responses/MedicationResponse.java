package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.MedicationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponse extends MedicationRequest {
    @Schema(description = "Id медицинского препарата")
    private Integer id;
}
