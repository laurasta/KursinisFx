package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String routeStartAddress;
    private String destinationAddress;
    @Enumerated
    private DestStatus destStatus;
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
    @ManyToOne
    private Trucker driver;
    @ManyToOne
    private Manager responsibleManager;
    @OneToMany(mappedBy = "checkpointDestination", cascade = CascadeType.ALL)
    private List<Checkpoint> checkpoints;

    public Destination(String title, String routeStartAddress, String destinationAddress, Manager responsibleManager) {
        this.title = title;
        this.routeStartAddress = routeStartAddress;
        this.destinationAddress = destinationAddress;
        this.destStatus = DestStatus.NEW;
        this.responsibleManager = responsibleManager;
        this.checkpoints = new ArrayList<>();
        this.arrivalDate = null;
        this.departureDate = null;
    }
}
