package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.MedSupplierRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedSupplierResponse extends MedSupplierRequest {
    @Schema(description = "ID поставщика")
    private Integer id;
}
