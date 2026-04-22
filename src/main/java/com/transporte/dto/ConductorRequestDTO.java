package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConductorRequestDTO {

    @NotNull(message = "El ID de persona es requerido")
    private Long idPersona;

    @NotNull(message = "El ID de vehículo es requerido")
    private Long idVehiculo;
}