package com.transporte.service;

import com.transporte.dto.*;
import com.transporte.entity.Documento;
import com.transporte.entity.Vehiculo;
import com.transporte.repository.DocumentoRepository;
import com.transporte.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional
    public List<DocumentoResponseDTO> subirDocumentos(DocumentoListaRequestDTO request) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.getIdVehiculo())
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));

        List<Documento> documentos = new ArrayList<>();

        for (DocumentoRequestDTO docRequest : request.getDocumentos()) {
            Documento documento = Documento.builder()
                    .tipoDocumento(docRequest.getTipoDocumento())
                    .numeroDocumento(docRequest.getNumeroDocumento())
                    .fechaVencimiento(docRequest.getFechaVencimiento())
                    .archivo(Base64.getDecoder().decode(docRequest.getArchivoBase64()))
                    .vehiculo(vehiculo)
                    .build();

            documentos.add(documento);
        }

        documentos = documentoRepository.saveAll(documentos);

        return documentos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DocumentoResponseDTO obtenerDocumento(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));
        return mapToResponse(documento);
    }

    private DocumentoResponseDTO mapToResponse(Documento documento) {
        String archivoBase64 = documento.getArchivo() != null 
                ? Base64.getEncoder().encodeToString(documento.getArchivo()) 
                : null;

        return DocumentoResponseDTO.builder()
                .id(documento.getId())
                .tipoDocumento(documento.getTipoDocumento())
                .numeroDocumento(documento.getNumeroDocumento())
                .fechaVencimiento(documento.getFechaVencimiento())
                .idVehiculo(documento.getVehiculo().getId())
                .placaVehiculo(documento.getVehiculo().getPlaca())
                .archivoBase64(archivoBase64)
                .build();
    }
}