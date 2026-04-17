package com.logistica.infrastructure.persistence.repositories;

import com.logistica.domain.enums.EstadoLiquidacion;
import com.logistica.domain.enums.TipoAjuste;
import com.logistica.infrastructure.persistence.entities.AjusteEntity;
import com.logistica.infrastructure.persistence.entities.LiquidacionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AjusteJpaRepository Integration Tests")
class AjusteRepositoryIT extends AbstractRepositoryIT {

    @Autowired
    private AjusteJpaRepository ajusteRepository;

    @Autowired
    private LiquidacionJpaRepository liquidacionRepository;

    @Test
    @DisplayName("Debe encontrar ajustes por el ID de la liquidación")
    void shouldFindByLiquidacionId() {
        // Given: Una liquidación persistida
        LiquidacionEntity liq = new LiquidacionEntity();
        liq.setId(UUID.randomUUID());
        liq.setIdRuta(UUID.randomUUID());
        liq.setIdContrato(UUID.randomUUID());
        liq.setEstado(EstadoLiquidacion.CALCULADA);
        liq.setValorBase(BigDecimal.TEN);
        liq.setValorFinal(BigDecimal.TEN);
        liq.setFechaCalculo(OffsetDateTime.now());
        
        // Asignar auditoría manual para evitar errores de restricción not null en tests
        liq.setCreatedAt(OffsetDateTime.now());
        liq.setUpdatedAt(OffsetDateTime.now());
        
        liquidacionRepository.saveAndFlush(liq);

        // Given: Dos ajustes asociados
        AjusteEntity a1 = new AjusteEntity();
        a1.setId(UUID.randomUUID());
        a1.setLiquidacion(liq);
        a1.setTipo(TipoAjuste.BONO);
        a1.setMonto(new BigDecimal("10.0000"));
        a1.setMotivo("Bono 1");
        a1.setCreatedAt(OffsetDateTime.now());
        a1.setUpdatedAt(OffsetDateTime.now());
        
        AjusteEntity a2 = new AjusteEntity();
        a2.setId(UUID.randomUUID());
        a2.setLiquidacion(liq);
        a2.setTipo(TipoAjuste.PENALIZACION);
        a2.setMonto(new BigDecimal("5.0000"));
        a2.setMotivo("Descuento 1");
        a2.setCreatedAt(OffsetDateTime.now());
        a2.setUpdatedAt(OffsetDateTime.now());
        
        ajusteRepository.saveAllAndFlush(List.of(a1, a2));

        // When
        List<AjusteEntity> results = ajusteRepository.findByLiquidacion_Id(liq.getId());

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).extracting(AjusteEntity::getMotivo)
                .containsExactlyInAnyOrder("Bono 1", "Descuento 1");
    }
}
