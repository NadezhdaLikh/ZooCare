package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.EnclosureRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureResponse extends EnclosureRequest {
    @Schema(description = "Id местообитания")
    private Integer id;
}
