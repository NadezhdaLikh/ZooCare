package com.spring.zoocare.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Schema(description = "Адрес корпоративной электронной почты сотрудника")
    @NotNull(message = "Необходимо указать корпоративный email.")
    private String email;

    @Schema(description = "Пароль сотрудника")
    @NotNull(message = "Необходимо указать пароль.")
    private String password;
}


