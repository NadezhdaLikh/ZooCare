package com.spring.zoocare.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ObservationVetRequest {
    @Schema(description = "Комментарии ветеринара")
    private String vetFeedback;
}
