package io.turntabl.project.reportingcontract.entities;

import io.turntabl.project.reportingcontract.enums.ClientEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Table(name = "client_report")
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEvent extends Event {
    @Column(nullable = false)
    private ClientEventType type;
    @Column(nullable = false)
    private UUID clientId;
}
