package com.transporte.repository;

import com.transporte.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v LEFT JOIN FETCH v.documentos WHERE v.id = :id")
    Optional<Vehiculo> findByIdWithDocumentos(@Param("id") Long id);

    @Query("SELECT v FROM Vehiculo v LEFT JOIN FETCH v.documentos WHERE v.placa = :placa")
    Optional<Vehiculo> findByPlacaWithDocumentos(@Param("placa") String placa);
}