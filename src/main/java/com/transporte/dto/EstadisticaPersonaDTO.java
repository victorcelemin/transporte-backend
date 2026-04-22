package com.transporte.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaPersonaDTO {

    private String tipoPersona;
    private Long total;
}