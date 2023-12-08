package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private String licenseNumber;
    private Boolean outOfService;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Trucker> truckDriver;

    public Vehicle(String model, String licenseNumber) {
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.outOfService = false;
        this.truckDriver = new ArrayList<>();
    }
}
