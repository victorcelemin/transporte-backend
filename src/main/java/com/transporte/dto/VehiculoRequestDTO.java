package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequestDTO {

    @NotBlank(message = "La placa es requerida")
    private String placa;

    @NotBlank(message = "La marca es requerida")
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    private String modelo;

    @NotNull(message = "El año de fabricación es requerido")
    @Min(value = 1900, message = "Año inválido")
    private Integer anioFabricacion;
}