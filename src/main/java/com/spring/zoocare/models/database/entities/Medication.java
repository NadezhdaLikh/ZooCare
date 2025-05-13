package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.MedCategory;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "medication")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private MedCategory medCategory;

    @Column(name = "is_now_in_use", columnDefinition = "boolean default true")
    private Boolean isNowInUse = true;

    @PrePersist
    public void setDefaultValues() {
        if (isNowInUse == null) {
            isNowInUse = true;
        }
    }
}