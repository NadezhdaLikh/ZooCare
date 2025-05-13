package com.spring.zoocare.models.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.zoocare.models.enums.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "species", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private Species species;

    @Column(name = "subspecies")
    private String subspecies;

    @Column(name = "name")
    private String name;

    @Column(name = "sex", columnDefinition = "varchar(6)")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "born_in_captivity", columnDefinition = "boolean default true")
    private Boolean bornInCaptivity = true;

    @Column(name = "place_of_birth", columnDefinition = "varchar(255) default 'Ленинградский зоопарк (Россия, г. Санкт-Петербург, Александровский парк, д. 1, литера А)'")
    private String placeOfBirth = "Ленинградский зоопарк (Россия, г. Санкт-Петербург, Александровский парк, д. 1, литера А)";

    /*@Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "cause_of_death", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private CauseOfDeath causeOfDeath;*/

    @Column(name = "weight")
    private Float weight;

    @Column(name = "height")
    private Float height;

    // The many side of many-to-one bidirectional relationships must not define the mappedBy element. The many side is always the owning side of the relationship.
    @ManyToOne
    @JoinColumn(name = "enclosure_id", nullable = false) // fetch = FetchType.EAGER
    @JsonBackReference
    /*
    Annotation used to indicate that associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link.
    Linkage is handled such that the property annotated with this annotation is not serialized; and during deserialization, its value is set to instance that has the "managed" (forward) link.
    */
    private Enclosure enclosure;

    @Column(name = "heath_status", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus = HealthStatus.UNKNOWN;

    @Column(name = "has_chronic_condition", columnDefinition = "boolean default false")
    private Boolean hasChronicCondition = false;

    @Column(name = "is_quarantined", columnDefinition = "boolean default false")
    private Boolean isQuarantined = false;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @PrePersist
    public void setDefaultValues() { // Apply default only for new entities
        if (bornInCaptivity == null) {
            bornInCaptivity = true;
        }
        if (healthStatus == null) {
            healthStatus = HealthStatus.UNKNOWN;
        }
        if (hasChronicCondition == null) {
            hasChronicCondition = false;
        }
        if (isQuarantined == null) {
            isQuarantined = false;
        }
        if (placeOfBirth == null) {
            placeOfBirth = "Ленинградский зоопарк (Россия, г. Санкт-Петербург, Александровский парк, д. 1, литера А)";
        }
    }
}