package com.spring.zoocare.models.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.zoocare.models.dtos.requests.AnimalRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalResponse extends AnimalRequest {
    @Schema(description = "Id животного")
    private Integer id;

    @Schema(description = "Сведения о местообитание, где проживает животное")
    private EnclosureResponse enclosureResponse;
}
