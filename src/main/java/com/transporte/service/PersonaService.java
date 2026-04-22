package com.transporte.service;

import com.transporte.dto.*;
import com.transporte.entity.Persona;
import com.transporte.entity.Usuario;
import com.transporte.repository.PersonaRepository;
import com.transporte.repository.UsuarioRepository;
import com.transporte.util.GeneradorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final GeneradorUtil generadorUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PersonaResponseDTO crearPersona(PersonaRequestDTO request) {
        if (personaRepository.existsByIdentificacion(request.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe una persona con esta identificación");
        }
        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una persona con este correo");
        }

        Persona persona = Persona.builder()
                .identificacion(request.getIdentificacion())
                .tipoIdentificacion(request.getTipoIdentificacion())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .tipoPersona(request.getTipoPersona())
                .build();

        persona = personaRepository.save(persona);

        String login = null;
        if ("A".equals(request.getTipoPersona())) {
            login = crearUsuario(persona);
        }

        return mapToResponse(persona, login);
    }

    private String crearUsuario(Persona persona) {
        if (usuarioRepository.existsByIdPersona(persona.getId())) {
            return null;
        }

        String login = generadorUtil.generarLogin(persona.getNombres(), persona.getIdentificacion());
        String password = generadorUtil.generarPassword();
        String apiKey = generadorUtil.generarApiKey();

        Usuario usuario = Usuario.builder()
                .login(login)
                .idPersona(persona.getId())
                .password(passwordEncoder.encode(password))
                .apiKey(apiKey)
                .build();

        usuarioRepository.save(usuario);
        return login;
    }

    @Transactional(readOnly = true)
    public List<PersonaResponseDTO> listarPersonas() {
        return personaRepository.findAll().stream()
                .map(p -> mapToResponse(p, null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PersonaResponseDTO obtenerPersona(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));
        
        String login = usuarioRepository.findByIdPersona(id)
                .map(Usuario::getLogin)
                .orElse(null);
        
        return mapToResponse(persona, login);
    }

    @Transactional
    public PersonaResponseDTO actualizarPersona(Long id, PersonaRequestDTO request) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        if (!persona.getIdentificacion().equals(request.getIdentificacion()) 
                && personaRepository.existsByIdentificacion(request.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe una persona con esta identificación");
        }
        if (!persona.getCorreo().equals(request.getCorreo()) 
                && personaRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una persona con este correo");
        }

        persona.setIdentificacion(request.getIdentificacion());
        persona.setTipoIdentificacion(request.getTipoIdentificacion());
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setCorreo(request.getCorreo());
        persona.setTipoPersona(request.getTipoPersona());

        persona = personaRepository.save(persona);

        String login = usuarioRepository.findByIdPersona(id)
                .map(Usuario::getLogin)
                .orElse(null);

        return mapToResponse(persona, login);
    }

    private PersonaResponseDTO mapToResponse(Persona persona, String login) {
        return PersonaResponseDTO.builder()
                .id(persona.getId())
                .identificacion(persona.getIdentificacion())
                .tipoIdentificacion(persona.getTipoIdentificacion())
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .correo(persona.getCorreo())
                .tipoPersona(persona.getTipoPersona())
                .login(login)
                .build();
    }
}