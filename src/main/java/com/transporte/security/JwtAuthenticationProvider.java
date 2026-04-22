package com.transporte.security;

import com.transporte.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final UsuarioRepository usuarioRepository;

    public boolean validateApiKey(String login, String apiKey) {
        return usuarioRepository.findByLogin(login)
                .map(u -> apiKey.equals(u.getApiKey()))
                .orElse(false);
    }
}