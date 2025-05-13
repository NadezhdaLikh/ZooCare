package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Species {
    AMPHIBIANS ("Амфибии"),
    BIRDS ("Птицы"),
    FISH ("Рыбы"),
    INSECTS ("Насекомые"),
    MAMMALS ("Млекопитающие"),
    REPTILES ("Рептилии");

    private final String value;
}
