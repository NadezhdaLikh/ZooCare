package com.spring.zoocare.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcedureType {
    SURGERY ("Хирургическая операция"),
    INJECTION ("Инъекция"),
    BANDAGING ("Перевязка"),
    WOUND_TREATMENT ("Уход за раной"),
    DENTAL_CARE ("Стоматологический уход"),
    VACCINATION("Вакцинация"),
    DEWORMING ("Дегельминтизация"),
    EUTHANASIA ("Эвтаназия");

    private final String value;
}
