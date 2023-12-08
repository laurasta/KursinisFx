package com.example.kursinisfx.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cargo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private String weight;
    private String cargoType;
    @ManyToOne
    private Destination cargoDestination;
    @ManyToOne
    private Checkpoint cargoCheckpoint;

    public Cargo(String content, String weight, Checkpoint cargoCheckpoint, Destination cargoDestination, String cargoType) {
        this.content = content;
        this.weight = weight;
        this.cargoCheckpoint = cargoCheckpoint;
        this.cargoDestination = cargoDestination;
        this.cargoType = cargoType;
    }
}