package com.transporte.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Usuario.UsuarioId.class)
public class Usuario implements Serializable {

    @Id
    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Id
    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "api_key", unique = true, length = 64)
    private String apiKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", insertable = false, updatable = false)
    private Persona persona;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioId implements Serializable {
        private String login;
        private Long idPersona;
    }
}