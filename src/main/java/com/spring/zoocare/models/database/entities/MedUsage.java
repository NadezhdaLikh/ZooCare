package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.AdministrationRoute;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "medication_usage")
public class MedUsage {

    @EmbeddedId
    private MedUsageId medUsageId;

    @Column(name = "dosage_used") // columnDefinition = "decimal(10, 2)"
    // The first number (10) defines the total length of the number (all digits, including those after the decimal point), while the second number (2) tells how many of them are after the decimal point.
    private Integer dosageUsed;
}
