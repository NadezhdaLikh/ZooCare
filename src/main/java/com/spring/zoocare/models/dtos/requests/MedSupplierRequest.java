package com.spring.zoocare.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedSupplierRequest {
    @Schema(description = "Название организации-поставщика")
    String name;

    @Schema(description = "ОГРН")
    String ogrn;

    @Schema(description = "ИНН")
    String inn;

    @Schema(description = "КПП")
    String kpp;

    @Schema(description = "Адрес электронной почты")
    String email;

    @Schema(description = "Номер телефона")
    String phoneNumber;

    @Schema(description = "Юридический адрес")
    String legalAddress;

    @Schema(description = "Осуществляет ли организация поставки в настоящее время")
    Boolean isNowSupplying;
}
