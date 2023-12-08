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
public class Checkpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String checkpointAddress;
    private Boolean isReached;
    @ManyToOne
    private Destination checkpointDestination;
    @OneToMany(mappedBy = "cargoCheckpoint", cascade = CascadeType.ALL)
    private List<Cargo> checkpointCargo;

    public Checkpoint( String title, String checkpointAddress, Destination checkpointDestination) {
        this.checkpointAddress = checkpointAddress;
        this.isReached = false;
        this.title = title;
        this.checkpointCargo = new ArrayList<>();
        this.checkpointDestination = checkpointDestination;
    }
}

