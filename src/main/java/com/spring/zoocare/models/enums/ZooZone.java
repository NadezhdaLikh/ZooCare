package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ZooZone {
    TROPICAL ("Тропики"),
    DESERT ("Пустыня"),
    GRASSLAND ("Луга"),
    FOREST ("Лес"),
    ARCTIC ("Арктика"),
    AQUATIC ("Водная среда");

    private final String value;
}