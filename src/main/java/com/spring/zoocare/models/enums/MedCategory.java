package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MedCategory {
    ANTIBIOTIC ("Антибиотики"),
    ANALGESIC ("Анальгетики"),
    SEDATIVE ("Седативные препараты"),
    ANTI_INFLAMMATORY ("Противовоспалительные препараты"),
    ANTIPARASITIC ("Противопаразитарные препараты"),
    VACCINE ("Вакцины"),
    VITAMIN ("Витамины"),
    HORMONAL ("Гормональные препараты"),
    EUTHANASIA_AGENT ("Препараты для эвтаназии");

    private final String value;
}