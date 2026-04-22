package com.transporte.controller;

import com.transporte.dto.*;
import com.transporte.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping("/{login}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable String login, 
            @Valid @RequestBody PasswordChangeDTO request) {
        usuarioService.cambiarPassword(login, request.getNuevaPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{login}/apikey")
    public ResponseEntity<UsuarioResponseDTO> regenerarApiKey(@PathVariable String login) {
        return ResponseEntity.ok(usuarioService.regenerarApiKey(login));
    }
}