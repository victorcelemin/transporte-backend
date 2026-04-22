package com.transporte.repository;

import com.transporte.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query("SELECT d FROM Documento d WHERE d.fechaVencimiento < :fecha")
    List<Documento> findDocumentosVencidos(@Param("fecha") LocalDate fecha);

    @Query("SELECT d FROM Documento d WHERE d.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin")
    List<Documento> findDocumentosPorVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT DISTINCT d.vehiculo FROM Documento d WHERE d.fechaVencimiento < :fecha")
    List<Documento> findVehiculosConDocumentosVencidos(@Param("fecha") LocalDate fecha);

    @Query("SELECT DISTINCT d.vehiculo FROM Documento d WHERE d.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin")
    List<Documento> findVehiculosConDocumentosPorVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}