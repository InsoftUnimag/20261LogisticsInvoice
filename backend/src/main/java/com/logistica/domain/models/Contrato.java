package com.logistica.domain.models;

import com.logistica.domain.enums.TipoContratacion;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class Contrato {

    private final UUID id;
    private final TipoContratacion tipoContratacion;
    private final BigDecimal tarifa;

    public Contrato(UUID id, TipoContratacion tipoContratacion, BigDecimal tarifa) {
        if (id == null) {
            throw new IllegalArgumentException("El id del contrato no puede ser null");
        }

        if (tipoContratacion == null) {
            throw new IllegalArgumentException("El tipo de contratación es obligatorio");
        }

        if (tarifa == null || tarifa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser mayor a 0");
        }

        this.id = id;
        this.tipoContratacion = tipoContratacion;
        this.tarifa = tarifa;
    }

    public boolean esPorParada() {
        return tipoContratacion == TipoContratacion.POR_PARADA;
    }

    public boolean esRecorridoCompleto() {
        return tipoContratacion == TipoContratacion.RECORRIDO_COMPLETO;
    }
}
