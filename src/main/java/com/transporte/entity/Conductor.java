package com.transporte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "conductor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conductor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    @NotNull(message = "La persona es requerida")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @NotNull(message = "El vehículo es requerido")
    private Vehiculo vehiculo;

    @Column(name = "fecha_asociacion", nullable = false)
    @NotNull(message = "La fecha de asociación es requerida")
    private LocalDate fechaAsociacion;

    @Column(name = "estado", nullable = false, length = 2)
    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(PO|EA|RO)$", message = "Solo se permite estado PO, EA o RO")
    private String estado;
}