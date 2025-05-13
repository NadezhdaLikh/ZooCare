package com.spring.zoocare.models.dtos.requests;

import com.spring.zoocare.models.enums.Role;
import com.spring.zoocare.models.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeRequest {
    @Schema(description = "Имя")
    String firstName;

    @Schema(description = "Отчество")
    String middleName;

    @Schema(description = "Фамилия")
    String lastName;

    @Schema(description = "Пол")
    Sex sex;

    @Schema(description = "Дата рождения")
    LocalDate birthDate;

    @Schema(description = "Занимаемая должность")
    String occupation;

    @Schema(description = "Дата начала работы в зоопарке")
    LocalDate whenStartedWork;

    @Schema(description = "Дата окончания работы в зоопарке")
    LocalDate whenQuitWork;

    @Schema(description = "Номер телефона")
    String phoneNumber;

    @Schema(description = "Адрес корпоративной электронной почты")
    @NotNull(message = "Необходимо указать адрес электронной почты сотрудника.")
    String email;

    @Schema(description = "Роль сотрудника в системе")
    Role role;
}
