package com.transporte.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoListaRequestDTO {

    private Long idVehiculo;
    private List<DocumentoRequestDTO> documentos;
}