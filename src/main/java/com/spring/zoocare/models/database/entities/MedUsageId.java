package com.spring.zoocare.models.database.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MedUsageId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "medical_procedure_id", nullable = false)
    private MedProcedure medProcedure;

    /*@ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;*/

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private BatchInStock batchInStock;
}
