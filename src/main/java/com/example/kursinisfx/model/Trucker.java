package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Trucker extends User implements Serializable{
    private Boolean healthCertificate;
    private String driverLicense;

    @ManyToOne
    private Vehicle vehicle;
    @OneToMany (mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Destination> myDestinations;


    public Trucker(String login, String password, String name, String surname, String email, String phoneNumber, Boolean healthCertificate, String driverLicense, Vehicle vehicle) {
        super(login, password, name, surname, email, phoneNumber);
        this.healthCertificate = healthCertificate;
        this.driverLicense = driverLicense;
        this.setUserType(UserType.TRUCKER);
        this.myDestinations = new ArrayList<>();
        this.vehicle = vehicle;
    }

}
