package com.logistica.infrastructure.persistence.repositories;

import com.logistica.domain.enums.EstadoLiquidacion;
import com.logistica.infrastructure.persistence.entities.LiquidacionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LiquidacionJpaRepository Integration Tests")
class LiquidacionRepositoryIT extends AbstractRepositoryIT {

    @Autowired
    private LiquidacionJpaRepository repository;

    @Test
    @DisplayName("Debe guardar y encontrar una liquidación por su ID")
    void shouldSaveAndFindById() {
        // Given
        UUID rutaId = UUID.randomUUID();
        LiquidacionEntity entity = new LiquidacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setIdRuta(rutaId);
        entity.setIdContrato(UUID.randomUUID());
        entity.setEstado(EstadoLiquidacion.CALCULADA);
        entity.setValorBase(new BigDecimal("100.0000"));
        entity.setValorFinal(new BigDecimal("100.0000"));
        entity.setFechaCalculo(OffsetDateTime.now());
        
        // Campos de BaseEntity (manuales para el test si PrePersist no se activa en DataJpaTest)
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        // When
        LiquidacionEntity saved = repository.saveAndFlush(entity);
        Optional<LiquidacionEntity> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getIdRuta()).isEqualTo(rutaId);
        assertThat(found.get().getEstado()).isEqualTo(EstadoLiquidacion.CALCULADA);
    }

    @Test
    @DisplayName("Debe verificar si existe una liquidación por idRuta")
    void shouldCheckExistsByIdRuta() {
        // Given
        UUID rutaId = UUID.randomUUID();
        LiquidacionEntity entity = new LiquidacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setIdRuta(rutaId);
        entity.setIdContrato(UUID.randomUUID());
        entity.setEstado(EstadoLiquidacion.CALCULADA);
        entity.setValorBase(BigDecimal.TEN);
        entity.setValorFinal(BigDecimal.TEN);
        entity.setFechaCalculo(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        
        repository.saveAndFlush(entity);

        // When
        boolean exists = repository.existsByIdRuta(rutaId);
        boolean notExists = repository.existsByIdRuta(UUID.randomUUID());

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
