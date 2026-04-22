package com.transporte.service;

import com.transporte.dto.*;
import com.transporte.entity.Conductor;
import com.transporte.entity.Persona;
import com.transporte.entity.Vehiculo;
import com.transporte.repository.ConductorRepository;
import com.transporte.repository.PersonaRepository;
import com.transporte.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final PersonaRepository personaRepository;
    private final ConductorRepository conductorRepository;

    @Transactional
    public VehiculoResponseDTO crearVehiculo(VehiculoRequestDTO request) {
        if (vehiculoRepository.existsByPlaca(request.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehículo con esta placa");
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .placa(request.getPlaca())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anioFabricacion(request.getAnioFabricacion())
                .build();

        vehiculo = vehiculoRepository.save(vehiculo);

        return mapToResponse(vehiculo);
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listarVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerVehiculo(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findByIdWithDocumentos(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));
        return mapToResponse(vehiculo);
    }

    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerVehiculoPorPlaca(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlacaWithDocumentos(placa)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));
        return mapToResponse(vehiculo);
    }

    @Transactional
    public ConductorResponseDTO asociarConductor(ConductorRequestDTO request) {
        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        if (!"C".equals(persona.getTipoPersona())) {
            throw new IllegalArgumentException("Solo personas tipo CONDUCTOR pueden ser asociadas a vehículos");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(request.getIdVehiculo())
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));

        conductorRepository.findByPersonaAndVehiculo(request.getIdPersona(), request.getIdVehiculo())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("El conductor ya está asociado a este vehículo");
                });

        Conductor conductor = Conductor.builder()
                .persona(persona)
                .vehiculo(vehiculo)
                .fechaAsociacion(LocalDate.now())
                .estado("PO")
                .build();

        conductor = conductorRepository.save(conductor);

        return mapConductorToResponse(conductor);
    }

    @Transactional
    public ConductorResponseDTO cambiarEstadoConductor(Long idConductor, ConductorEstadoDTO request) {
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));

        conductor.setEstado(request.getEstado());
        conductor = conductorRepository.save(conductor);

        return mapConductorToResponse(conductor);
    }

    @Transactional(readOnly = true)
    public List<ConductorResponseDTO> listarConductoresHabilitados() {
        return conductorRepository.findByEstado("PO").stream()
                .map(this::mapConductorToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listarVehiculosConDocumentosVencidos() {
        LocalDate fechaActual = LocalDate.now();
        return vehiculoRepository.findAll().stream()
                .filter(v -> v.getDocumentos() != null && v.getDocumentos().stream()
                        .anyMatch(d -> d.getFechaVencimiento().isBefore(fechaActual)))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listarVehiculosConDocumentosPorVencer(int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = LocalDate.now().plusDays(dias);
        return vehiculoRepository.findAll().stream()
                .filter(v -> v.getDocumentos() != null && v.getDocumentos().stream()
                        .anyMatch(d -> !d.getFechaVencimiento().isBefore(fechaInicio) 
                                && !d.getFechaVencimiento().isAfter(fechaFin)))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private VehiculoResponseDTO mapToResponse(Vehiculo vehiculo) {
        List<ConductorResponseDTO> conductores = conductorRepository.findByVehiculo(vehiculo.getId()).stream()
                .map(this::mapConductorToResponse)
                .collect(Collectors.toList());

        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .placa(vehiculo.getPlaca())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anioFabricacion(vehiculo.getAnioFabricacion())
                .conductores(conductores)
                .documentos(null)
                .build();
    }

    private ConductorResponseDTO mapConductorToResponse(Conductor conductor) {
        return ConductorResponseDTO.builder()
                .id(conductor.getId())
                .idPersona(conductor.getPersona().getId())
                .nombresPersona(conductor.getPersona().getNombres())
                .apellidosPersona(conductor.getPersona().getApellidos())
                .idVehiculo(conductor.getVehiculo().getId())
                .placaVehiculo(conductor.getVehiculo().getPlaca())
                .fechaAsociacion(conductor.getFechaAsociacion())
                .estado(conductor.getEstado())
                .build();
    }
}