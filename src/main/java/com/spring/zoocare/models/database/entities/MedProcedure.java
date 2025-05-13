package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.ProcedureType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "medical_procedure")
public class MedProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "vet_in_charge", nullable = false)
    private Employee vet;

    @Column(name = "type", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private ProcedureType procedureType;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "performed_on")
    private LocalDate performedOn;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}

