package com.transporte.repository;

import com.transporte.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByIdentificacion(String identificacion);

    Optional<Persona> findByCorreo(String correo);

    List<Persona> findByTipoPersona(String tipoPersona);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByCorreo(String correo);
}