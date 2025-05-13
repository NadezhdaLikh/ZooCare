package com.spring.zoocare.models.database.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "observation")
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "zookeeper_id", nullable = false)
    private Employee zookeeper;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(name = "made_on")
    private LocalDate madeOn;

    @Column(name = "is_urgent", columnDefinition = "boolean default false")
    private Boolean isUrgent = false;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "is_checked_by_vet", columnDefinition = "boolean default false")
    private Boolean isCheckedByVet = false;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Employee vet;

    @Column(name = "vet_feedback", columnDefinition = "text")
    private String vetFeedback;

    @PrePersist
    public void setDefaultValues() {
        if (isUrgent == null) {
            isUrgent = false;
        }
        if (isCheckedByVet == null) {
            isCheckedByVet = false;
        }
    }
}
