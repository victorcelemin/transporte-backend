package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRequestDTO {

    @NotBlank(message = "La identificación es requerida")
    private String identificacion;

    @NotBlank(message = "El tipo de identificación es requerido")
    @Pattern(regexp = "^CC$", message = "Solo se permite tipo CC")
    private String tipoIdentificacion;

    @NotBlank(message = "Los nombres son requeridos")
    private String nombres;

    @NotBlank(message = "Los apellidos son requeridos")
    private String apellidos;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Correo inválido")
    private String correo;

    @NotBlank(message = "El tipo de persona es requerido")
    @Pattern(regexp = "^[CA]$", message = "Solo se permite tipo C (Conductor) o A (Administrativo)")
    private String tipoPersona;
}