package com.transporte.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoResponseDTO {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anioFabricacion;
    private List<ConductorResponseDTO> conductores;
    private List<DocumentoResponseDTO> documentos;
}