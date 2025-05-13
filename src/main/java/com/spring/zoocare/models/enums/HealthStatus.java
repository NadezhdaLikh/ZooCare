package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HealthStatus {
    HEALTHY ("Здоров"),
    UNDER_WATCH ("Под наблюдением"),
    SICK ("Болен"),
    RECOVERING ("В процессе восстановления"),
    CRITICAL ("Критическое"),
    DECEASED ("Умер"),
    UNKNOWN ("Неизвестно");

    private final String value;
}

