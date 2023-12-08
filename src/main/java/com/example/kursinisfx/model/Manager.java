package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manager extends User implements Serializable {

    private String workEmail;
    private Boolean isAdmin;
    @OneToMany (mappedBy = "responsibleManager", cascade = CascadeType.ALL)
    private List<Destination> responsibleForDestinations;
    public Manager(String login, String password, String name, String surname, String email, String phoneNumber, String workEmail, Boolean isAdmin) {
        super(login, password, name, surname, email, phoneNumber);
        this.workEmail = workEmail;
        this.isAdmin = isAdmin;
        this.setUserType(UserType.MANAGER);
        this.responsibleForDestinations = new ArrayList<>();
    }
}
