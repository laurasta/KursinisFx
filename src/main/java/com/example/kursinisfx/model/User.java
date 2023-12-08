package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public abstract class User implements Serializable {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    @Enumerated
    private UserType userType;
    private LocalDate employmentDate;



    public User(String login, String password, String name, String surname, String email, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.employmentDate = LocalDate.now();
    }
}
