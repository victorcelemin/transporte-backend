package com.transporte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", nullable = false, unique = true, length = 10)
    @NotBlank(message = "La placa es requerida")
    private String placa;

    @Column(name = "marca", nullable = false, length = 50)
    @NotBlank(message = "La marca es requerida")
    private String marca;

    @Column(name = "modelo", nullable = false, length = 50)
    @NotBlank(message = "El modelo es requerido")
    private String modelo;

    @Column(name = "anio_fabricacion", nullable = false)
    @NotNull(message = "El año de fabricación es requerido")
    @Min(value = 1900, message = "Año inválido")
    private Integer anioFabricacion;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Conductor> conductores = new ArrayList<>();

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Documento> documentos = new ArrayList<>();
}