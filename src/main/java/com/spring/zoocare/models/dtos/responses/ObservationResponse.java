package com.spring.zoocare.models.dtos.responses;

import com.spring.zoocare.models.dtos.requests.ObservationZookeeperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
// @JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObservationResponse extends ObservationZookeeperRequest {
    @Schema(description = "Id наблюдения")
    Integer id;

    @Schema(description = "Заверил ли наблюдение ветеринар")
    Boolean isCheckedByVet;

    @Schema(description = "Id заверившего ветеринара")
    Integer vetId;

    @Schema(description = "Комментарии ветеринара")
    String vetFeedback;
}
