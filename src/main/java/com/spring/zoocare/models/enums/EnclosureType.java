package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnclosureType {
    TERRARIUM ("Террариум"),
    AQUARIUM ("Аквариум"),
    AVIARY ("Вольер"),
    CAGE ("Клетка"),
    CORRAL ("Загон");

    private final String value;
}
