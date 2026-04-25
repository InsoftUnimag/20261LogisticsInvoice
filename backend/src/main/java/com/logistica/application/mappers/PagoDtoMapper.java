package com.logistica.application.mappers;

import com.logistica.application.dtos.response.EstadoPagoResponseDTO;
import com.logistica.domain.models.Pago;

public class PagoDtoMapper {

    public EstadoPagoResponseDTO toEstadoPagoResponseDTO(Pago pago) {
        if (pago == null) {
            return null;
        }
        return new EstadoPagoResponseDTO(
                pago.getId(),
                pago.getEstado().name(),
                pago.getFecha(),
                pago.getMontoNeto(),
                null, // Motivo de rechazo no está en el modelo de dominio
                pago.getLiquidacionId()
        );
    }
}
