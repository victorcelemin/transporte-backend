package com.transporte.repository;

import com.transporte.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    List<Conductor> findByEstado(String estado);

    @Query("SELECT c FROM Conductor c WHERE c.persona.id = :idPersona AND c.vehiculo.id = :idVehiculo")
    Optional<Conductor> findByPersonaAndVehiculo(@Param("idPersona") Long idPersona, @Param("idVehiculo") Long idVehiculo);

    @Query("SELECT c FROM Conductor c WHERE c.vehiculo.id = :idVehiculo")
    List<Conductor> findByVehiculo(@Param("idVehiculo") Long idVehiculo);
}