package com.spring.zoocare.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @Schema(description = "Текущий пароль")
    @NotNull(message = "Необходимо указать текущий пароль.")
    private String currentPassword;

    @Schema(description = "Новый пароль")
    @NotNull(message = "Необходимо указать новый пароль.")
    private String newPassword;
}
