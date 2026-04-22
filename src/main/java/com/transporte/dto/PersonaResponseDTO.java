package com.transporte.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponseDTO {

    private Long id;
    private String identificacion;
    private String tipoIdentificacion;
    private String nombres;
    private String apellidos;
    private String correo;
    private String tipoPersona;
    private String login;
}