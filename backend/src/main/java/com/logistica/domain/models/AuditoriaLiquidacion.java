package com.logistica.domain.models;

import lombok.Builder;
import lombok.Getter;
import com.logistica.domain.enums.TipoOperacion;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class AuditoriaLiquidacion {

    private final UUID id;
    private final UUID idLiquidacion;
    private final TipoOperacion operacion;
    private final BigDecimal valorAnterior;
    private final BigDecimal valorNuevo;
    private final OffsetDateTime fechaOperacion;
    private final String responsable;


    public static AuditoriaLiquidacion crearCalculo(
            UUID idLiquidacion,
            BigDecimal valorNuevo
    ) {
        if (valorNuevo == null){
            throw new IllegalArgumentException("El valor nuevo no puede ser nulo");
        }
        return AuditoriaLiquidacion.builder()
                .id(UUID.randomUUID())
                .idLiquidacion(idLiquidacion)
                .operacion(TipoOperacion.CALCULO)
                .valorAnterior(null)
                .valorNuevo(valorNuevo)
                .fechaOperacion(OffsetDateTime.now())
                .responsable("SISTEMA")
                .build();
    }


    public static AuditoriaLiquidacion crearRecalculo(
            UUID idLiquidacion,
            BigDecimal valorAnterior,
            BigDecimal valorNuevo,
            String responsable // a futuro debe ser un usuario o tipo de usuario
    ) {
        if (idLiquidacion == null) {
            throw new IllegalArgumentException("El id de la liquidación no puede ser null");
        }

        if (valorNuevo == null) {
            throw new IllegalArgumentException("El valor nuevo no puede ser null");
        }

        if (valorAnterior == null) {
            throw new IllegalArgumentException("El valor anterior no puede ser null");
        }

        if (responsable == null || responsable.isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }

        return AuditoriaLiquidacion.builder()
                .id(UUID.randomUUID())
                .idLiquidacion(idLiquidacion)
                .operacion(TipoOperacion.RECALCULO)
                .valorAnterior(valorAnterior)
                .valorNuevo(valorNuevo)
                .fechaOperacion(OffsetDateTime.now())
                .responsable(responsable)
                .build();
    }
}
