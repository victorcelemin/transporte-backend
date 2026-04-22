package com.transporte.service;

import com.transporte.dto.*;
import com.transporte.entity.Usuario;
import com.transporte.repository.UsuarioRepository;
import com.transporte.util.GeneradorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final GeneradorUtil generadorUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void cambiarPassword(String login, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioResponseDTO regenerarApiKey(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        String nuevoApiKey = generadorUtil.generarApiKey();
        usuario.setApiKey(nuevoApiKey);
        usuarioRepository.save(usuario);

        return UsuarioResponseDTO.builder()
                .login(usuario.getLogin())
                .idPersona(usuario.getIdPersona())
                .apiKey(nuevoApiKey)
                .build();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuario(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return UsuarioResponseDTO.builder()
                .login(usuario.getLogin())
                .idPersona(usuario.getIdPersona())
                .apiKey(usuario.getApiKey())
                .build();
    }
}