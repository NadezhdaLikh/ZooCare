package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
    MALE("М"),
    FEMALE("Ж");

    private final String value;
}
