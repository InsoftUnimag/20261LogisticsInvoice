package com.logistica.contratos.infrastructure.persistence.repositories;

import com.logistica.contratos.infrastructure.persistence.entities.ContratoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContratoJpaRepository extends JpaRepository<ContratoEntity, Long> {

    @EntityGraph(attributePaths = {"usuario", "vehiculo", "usuario.seguros"})
    Optional<ContratoEntity> findByIdContrato(String idContrato);

    boolean existsByIdContrato(String idContrato);
}
