package com.transporte.controller;

import com.transporte.dto.*;
import com.transporte.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public ResponseEntity<PersonaResponseDTO> crearPersona(@Valid @RequestBody PersonaRequestDTO request) {
        return new ResponseEntity<>(personaService.crearPersona(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PersonaResponseDTO>> listarPersonas() {
        return ResponseEntity.ok(personaService.listarPersonas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> obtenerPersona(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPersona(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> actualizarPersona(
            @PathVariable Long id, 
            @Valid @RequestBody PersonaRequestDTO request) {
        return ResponseEntity.ok(personaService.actualizarPersona(id, request));
    }
}