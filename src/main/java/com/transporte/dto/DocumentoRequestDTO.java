package com.transporte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoRequestDTO {

    @NotBlank(message = "El tipo de documento es requerido")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es requerido")
    private String numeroDocumento;

    @NotNull(message = "La fecha de vencimiento es requerida")
    private LocalDate fechaVencimiento;

    @NotBlank(message = "El archivo en BASE64 es requerido")
    private String archivoBase64;
}