package com.transporte.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private String login;
    private Long idPersona;
    private String apiKey;
}