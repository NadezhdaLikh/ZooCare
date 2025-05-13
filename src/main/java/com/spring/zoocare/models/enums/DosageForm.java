package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DosageForm {
    TABLET ("Таблетки"),
    CAPSULE ("Капсулы"),
    ORAL_SOLUTION ("Пероральный раствор"),
    POWDER ("Порошок"),
    INJECTION_SOLUTION ("Раствор для инъекций");

    private final String value;
}