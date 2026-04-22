package com.transporte.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConductorResponseDTO {

    private Long id;
    private Long idPersona;
    private String nombresPersona;
    private String apellidosPersona;
    private Long idVehiculo;
    private String placaVehiculo;
    private LocalDate fechaAsociacion;
    private String estado;
}