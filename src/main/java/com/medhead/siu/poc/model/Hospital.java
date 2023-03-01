package com.medhead.siu.poc.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "hospital")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    @Column(name = "available_beds")
    private Integer availableBeds;

    private String address1;

    private String address2;

    private String address3;

    private String city;

    private String county;

    private String postcode;

    private String phone;

    private String email;

    @ManyToMany
    @JoinTable( name = "mtm_speciality_hospital",
            joinColumns = @JoinColumn( name = "hospital_id" ),
            inverseJoinColumns = @JoinColumn( name = "speciality_id" ) )
    @JsonManagedReference
    private List<Speciality> specialities = new ArrayList<>();
}
