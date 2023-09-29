package io.turntabl.project.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import io.turntabl.project.clientprocessingapi.enums.Role;

import java.util.UUID;


@Table(name = "client")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {


    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;


    public Client(String name, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }







}
