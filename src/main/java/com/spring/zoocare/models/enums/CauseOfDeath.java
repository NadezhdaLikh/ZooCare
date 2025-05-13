package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CauseOfDeath {
    NATURAL ("Естественная смерть"),
    DISEASE ("Болезнь"),
    ACCIDENT ("Несчастный случай"),
    EUTHANASIA ("Эвтаназия");

    private final String value;
}