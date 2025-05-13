package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter@RequiredArgsConstructor
public enum ExaminationType {
    SCHEDULED ("Плановый"),
    EMERGENCY ("Экстренный");

    private final String value;
}
