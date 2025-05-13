package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.MedProcedureRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedProcedureResponse extends MedProcedureRequest {
    @Schema(description = "Id медицинской процедуры")
    private Integer id;
}
