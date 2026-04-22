package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El login es requerido")
    private String login;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}