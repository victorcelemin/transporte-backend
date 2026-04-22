package com.transporte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_documento", nullable = false, length = 50)
    @NotBlank(message = "El tipo de documento es requerido")
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El número de documento es requerido")
    private String numeroDocumento;

    @Column(name = "fecha_vencimiento", nullable = false)
    @NotNull(message = "La fecha de vencimiento es requerida")
    private LocalDate fechaVencimiento;

    @Lob
    @Column(name = "archivo", columnDefinition = "LONGBLOB")
    private byte[] archivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @NotNull(message = "El vehículo es requerido")
    private Vehiculo vehiculo;
}