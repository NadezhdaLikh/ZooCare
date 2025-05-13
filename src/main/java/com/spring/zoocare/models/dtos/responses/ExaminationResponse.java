package com.spring.zoocare.models.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.zoocare.models.dtos.requests.ExaminationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResponse extends ExaminationRequest {
    @Schema(description = "Id осмотра")
    private Integer id;
}
