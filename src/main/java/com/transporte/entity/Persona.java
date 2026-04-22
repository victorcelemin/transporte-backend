package com.transporte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    @NotBlank(message = "La identificación es requerida")
    private String identificacion;

    @Column(name = "tipo_identificacion", nullable = false, length = 2)
    @NotBlank(message = "El tipo de identificación es requerido")
    @Pattern(regexp = "^CC$", message = "Solo se permite tipo CC")
    private String tipoIdentificacion;

    @Column(name = "nombres", nullable = false, length = 100)
    @NotBlank(message = "Los nombres son requeridos")
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    @NotBlank(message = "Los apellidos son requeridos")
    private String apellidos;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    @NotBlank(message = "El correo es requerido")
    @Email(message = "Correo inválido")
    private String correo;

    @Column(name = "tipo_persona", nullable = false, length = 1)
    @NotBlank(message = "El tipo de persona es requerido")
    @Pattern(regexp = "^[CA]$", message = "Solo se permite tipo C (Conductor) o A (Administrativo)")
    private String tipoPersona;
}