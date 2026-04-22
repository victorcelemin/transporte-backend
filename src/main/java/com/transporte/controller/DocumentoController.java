package com.transporte.controller;

import com.transporte.dto.*;
import com.transporte.service.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @PostMapping
    public ResponseEntity<List<DocumentoResponseDTO>> subirDocumentos(
            @Valid @RequestBody DocumentoListaRequestDTO request) {
        return new ResponseEntity<>(documentoService.subirDocumentos(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> obtenerDocumento(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.obtenerDocumento(id));
    }
}