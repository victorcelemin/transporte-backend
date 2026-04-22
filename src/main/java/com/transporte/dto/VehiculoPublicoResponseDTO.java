package com.transporte.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoPublicoResponseDTO {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anioFabricacion;
}