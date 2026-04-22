package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String nuevaPassword;
}