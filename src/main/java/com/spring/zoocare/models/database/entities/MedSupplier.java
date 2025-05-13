package com.spring.zoocare.models.database.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "medication_supplier", uniqueConstraints = {
        @UniqueConstraint(name = "ogrn_CNSTR", columnNames = "ogrn"),
        @UniqueConstraint(name = "inn_CNSTR", columnNames = "inn"),
        @UniqueConstraint(name = "kpp_CNSTR", columnNames = "kpp")})
public class MedSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "ogrn", columnDefinition = "varchar(13)")
    private String ogrn;

    @Column(name = "inn", columnDefinition = "varchar(10)")
    private String inn;

    @Column(name = "kpp", columnDefinition = "varchar(9)")
    private String kpp;

    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;

    @Column(name = "phone_number", columnDefinition = "varchar(11)")
    private String phoneNumber;

    @Column(name = "legal_address", columnDefinition = "varchar(100)")
    private String legalAddress;

    @Column(name = "is_now_supplying", columnDefinition = "boolean default true")
    private Boolean isNowSupplying = true;

    @PrePersist
    public void setDefaultValues() {
        if (isNowSupplying == null) {
            isNowSupplying = true;
        }
    }
}