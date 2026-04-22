package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConductorEstadoDTO {

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(PO|EA|RO)$", message = "Solo se permite estado PO, EA o RO")
    private String estado;
}