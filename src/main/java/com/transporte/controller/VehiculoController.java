package com.transporte.controller;

import com.transporte.dto.*;
import com.transporte.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> crearVehiculo(@Valid @RequestBody VehiculoRequestDTO request) {
        return new ResponseEntity<>(vehiculoService.crearVehiculo(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> listarVehiculos() {
        return ResponseEntity.ok(vehiculoService.listarVehiculos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> obtenerVehiculo(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculo(id));
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VehiculoResponseDTO> obtenerVehiculoPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculoPorPlaca(placa));
    }

    @PostMapping("/conductores")
    public ResponseEntity<ConductorResponseDTO> asociarConductor(@Valid @RequestBody ConductorRequestDTO request) {
        return new ResponseEntity<>(vehiculoService.asociarConductor(request), HttpStatus.CREATED);
    }

    @PutMapping("/conductores/{id}/estado")
    public ResponseEntity<ConductorResponseDTO> cambiarEstadoConductor(
            @PathVariable Long id, 
            @Valid @RequestBody ConductorEstadoDTO request) {
        return ResponseEntity.ok(vehiculoService.cambiarEstadoConductor(id, request));
    }

    @GetMapping("/conductores/habilitados")
    public ResponseEntity<List<ConductorResponseDTO>> listarConductoresHabilitados() {
        return ResponseEntity.ok(vehiculoService.listarConductoresHabilitados());
    }

    @GetMapping("/documentos/vencidos")
    public ResponseEntity<List<VehiculoResponseDTO>> listarVehiculosConDocumentosVencidos() {
        return ResponseEntity.ok(vehiculoService.listarVehiculosConDocumentosVencidos());
    }

    @GetMapping("/documentos/por-vencer")
    public ResponseEntity<List<VehiculoResponseDTO>> listarVehiculosConDocumentosPorVencer(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(vehiculoService.listarVehiculosConDocumentosPorVencer(dias));
    }
}