package com.transporte.controller;

import com.transporte.dto.*;
import com.transporte.repository.PersonaRepository;
import com.transporte.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoController {

    private final VehiculoService vehiculoService;
    private final PersonaRepository personaRepository;

    @GetMapping("/vehiculos/documentos-vencidos")
    public ResponseEntity<List<VehiculoResponseDTO>> vehiculosConDocumentosVencidos() {
        return ResponseEntity.ok(vehiculoService.listarVehiculosConDocumentosVencidos());
    }

    @GetMapping("/conductores/habilitados")
    public ResponseEntity<List<ConductorResponseDTO>> conductoresHabilitados() {
        return ResponseEntity.ok(vehiculoService.listarConductoresHabilitados());
    }

    @GetMapping("/vehiculos/{placa}")
    public ResponseEntity<VehiculoResponseDTO> consultarVehiculo(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculoPorPlaca(placa));
    }

    @GetMapping("/vehiculos/documentos-por-vencer")
    public ResponseEntity<List<VehiculoResponseDTO>> vehiculosConDocumentosPorVencer(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(vehiculoService.listarVehiculosConDocumentosPorVencer(dias));
    }

    @GetMapping("/personas/estadisticas")
    public ResponseEntity<List<EstadisticaPersonaDTO>> estadisticasPersonas() {
        List<String> tipos = List.of("C", "A");
        List<EstadisticaPersonaDTO> resultados = tipos.stream()
                .map(tipo -> {
                    long count = personaRepository.findByTipoPersona(tipo).size();
                    return EstadisticaPersonaDTO.builder()
                            .tipoPersona(tipo)
                            .total(count)
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultados);
    }
}