package com.spring.zoocare.models.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.zoocare.models.enums.EnclosureType;
import com.spring.zoocare.models.enums.ZooZone;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "enclosure")
public class Enclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "zoo_zone", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private ZooZone zooZone;

    @Column(name = "type", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private EnclosureType enclosureType;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_now_inhabitable", columnDefinition = "boolean default true")
    private Boolean isNowInhabitable = true;

    @OneToMany(mappedBy = "enclosure", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // fetch = FetchType.LAZY by default
    // Cascading is always applied on the parent side of the relationship.
    @JsonManagedReference
    /*
    Annotation used to indicate that annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link.
    Value type (class) of property must have a single compatible property annotated with JsonBackReference.
    Linkage is handled such that the property annotated with this annotation is handled normally (serialized normally, no special handling for deserialization); it is the matching back reference that requires special handling.
    */
    private List<Animal> animals;

    @PrePersist
    public void setDefaultValues() {
        if (isNowInhabitable == null) {
            isNowInhabitable = true;
        }
    }
}


