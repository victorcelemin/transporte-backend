package com.transporte.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoResponseDTO {

    private Long id;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaVencimiento;
    private Long idVehiculo;
    private String placaVehiculo;
    private String archivoBase64;
}