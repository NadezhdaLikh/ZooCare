package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.EnclosureType;
import com.spring.zoocare.models.enums.ZooZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnclosureRequest {
    @Schema(description = "Зона зоопарка")
    ZooZone zooZone;

    @Schema(description = "Тип местообитания")
    EnclosureType enclosureType;

    @Schema(description = "Вместимость местообитания")
    Integer capacity;

    @Schema(description = "Пригодно ли местообитание для жизни на данный момент")
    Boolean isNowInhabitable;
}
