package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdministrationRoute {
    ENTERAL ("Энтеральный"),
    PARENTERAL ("Парентеральный"),
    TOPICAL ("Местный"),
    INHALATION ("Ингаляционный");

    private final String value;
}

