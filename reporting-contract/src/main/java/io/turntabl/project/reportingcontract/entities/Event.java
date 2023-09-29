package io.turntabl.project.reportingcontract.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "happened_on", nullable = false)
    private LocalDateTime happenedOn;
    @Column(nullable = false)
    private String description;
}
