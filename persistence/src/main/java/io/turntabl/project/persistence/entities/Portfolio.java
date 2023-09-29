package io.turntabl.project.persistence.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "portfolio")
@Entity
@Getter
@Setter
public class Portfolio {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,
        name = "is_default")
    private boolean defaultPortfolio;
    @ManyToOne
    @JoinColumn(name = "client",
            nullable = false)
    private Client client;
}
