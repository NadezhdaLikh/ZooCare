package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.AdministrationRoute;
import com.spring.zoocare.models.enums.DosageForm;
import com.spring.zoocare.models.enums.DosageUnits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "batch_in_stock")
public class BatchInStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer Id;

    @ManyToOne // fetch = FetchType.EAGER by default
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private MedSupplier medSupplier;

    @Column(name = "dosage_form", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private DosageForm dosageForm;

    @Column(name = "dosage_unit", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private DosageUnits dosageUnits;

    @Column(name = "total_num_of_units")
    private Integer totalNumOfUnits;

    @Column(name = "administration_route", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private AdministrationRoute administrationRoute;

    @Column(name = "delivered_on")
    private LocalDate deliveredOn;

    @Column(name = "delivered_from")
    private String deliveredFrom;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
