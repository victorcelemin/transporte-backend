package com.transporte.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tipo;
    private String login;
    private Long expiresIn;
}