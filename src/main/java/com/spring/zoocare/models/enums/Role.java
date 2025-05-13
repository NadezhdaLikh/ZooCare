package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ZOOKEEPER("Смотритель"),
    VET("Ветеринар"),
    ADMIN("Администратор");

    private final String value;
}
