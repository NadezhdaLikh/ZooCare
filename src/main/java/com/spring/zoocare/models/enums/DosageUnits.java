package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DosageUnits {
    /*TABLET ("Таблетки"),
    CAPSULE ("Капсулы"),
    SACHET ("Саше"),
    AMPOULE ("Ампула"),*/
    G ("Гр"),
    MG ("Мг"),
    ML ("Мл"),
    L ("Л");

    private final String value;
}