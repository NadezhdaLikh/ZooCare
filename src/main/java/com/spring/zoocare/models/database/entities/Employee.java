package com.spring.zoocare.models.database.entities;

import com.spring.zoocare.models.enums.Sex;
import com.spring.zoocare.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "employee", uniqueConstraints = @UniqueConstraint(name = "email_CNSTR", columnNames = "email")) // @UniqueConstraint(name = "phone_number_CNSTR", columnNames = "phone_number")
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "sex", columnDefinition = "varchar(6)")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "occupation", columnDefinition = "varchar(100)")
    private String occupation;

    @Column(name = "when_started_work")
    private LocalDate whenStartedWork;

    @Column(name = "when_quit_work")
    private LocalDate whenQuitWork;

    @Column(name = "is_on_staff", columnDefinition = "boolean default true")
    private Boolean isOnStaff = true;

    @Column(name = "phone_number", columnDefinition = "varchar(11)")
    private String phoneNumber;

    @Column(name = "email", columnDefinition = "varchar(100) not null", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void setDefaultValues() {
        if (isOnStaff == null) {
            isOnStaff = true;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
