package com.spring.zoocare.models.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.zoocare.models.dtos.requests.EmployeeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse extends EmployeeRequest {
    @Schema(description = "Id сотрудника")
    private Integer id;

    @Schema(description = "Числится ли сотрудник в штате зоопарка на данный момент")
    private Boolean isOnStaff;
}
