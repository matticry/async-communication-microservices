package com.matticry.microservicesproject.model;

import com.matticry.microservicesproject.model.util.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_per")
    private Long id;

    @Column(name = "name_per")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre_per")
    private Genre genre;

    @Column(name = "age_per")
    private Integer age;

    @Column(name = "dni_per", length = 50, unique = true)
    private String dni;

    @Column(name = "address_per")
    private String address;

    @Column(name = "phone_per")
    private String phone;
}
