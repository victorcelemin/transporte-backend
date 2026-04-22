package com.transporte.repository;

import com.transporte.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Usuario.UsuarioId> {

    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByApiKey(String apiKey);

    boolean existsByLogin(String login);

    boolean existsByIdPersona(Long idPersona);
}