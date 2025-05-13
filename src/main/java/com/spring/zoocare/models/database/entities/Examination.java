package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.ExaminationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "examination")
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Employee vet;

    @Column(name = "type", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private ExaminationType examinationType = ExaminationType.SCHEDULED;

    @Column(name = "performed_on")
    private LocalDate performedOn;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "diagnosis", columnDefinition = "text")
    private String diagnosis;

    @Column(name = "prescription", columnDefinition = "text")
    private String prescription;

    @PrePersist
    public void setDefaultValues() {
        if (examinationType == null) {
            examinationType = ExaminationType.SCHEDULED;
        }
    }
}