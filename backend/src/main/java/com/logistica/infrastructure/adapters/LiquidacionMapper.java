package com.logistica.infrastructure.adapters;

import com.logistica.application.dtos.response.AjusteLiquidacionDTO;
import com.logistica.application.dtos.response.LiquidacionDetalleDTO;
import com.logistica.application.dtos.response.LiquidacionListItemDTO;
import com.logistica.domain.models.Ajuste;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.models.Ruta;
import com.logistica.infrastructure.persistence.entities.AjusteEntity;
import com.logistica.infrastructure.persistence.entities.LiquidacionEntity;
import com.logistica.infrastructure.persistence.entities.RutaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class LiquidacionMapper {

    // ── Entity → Domain ──────────────────────────────────────────────────────

    public Liquidacion toDomain(LiquidacionEntity entity) {
        return Liquidacion.builder()
                .id(entity.getId())
                .idRuta(entity.getRuta() != null ? entity.getRuta().getId() : null)
                .idContrato(entity.getIdContrato())
                .estadoLiquidacion(entity.getEstadoLiquidacion())
                .montoBruto(entity.getMontoBruto())
                .montoNeto(entity.getMontoNeto())
                .fechaCalculo(entity.getFechaCalculo())
                .usuarioId(entity.getUsuarioId())
                .ruta(toRutaDomain(entity.getRuta()))
                .ajustes(toAjustesDomain(entity.getAjustes()))
                .build();
    }

    private Ruta toRutaDomain(RutaEntity entity) {
        if (entity == null) return null;
        return Ruta.builder()
                .id(entity.getId())
                .fechaInicio(entity.getFechaInicio())
                .fechaCierre(entity.getFechaCierre())
                .tipoVehiculo(entity.getTipoVehiculo())
                .precioParada(entity.getPrecioParada())
                .numeroParadas(entity.getNumeroParadas())
                .build();
    }

    private List<Ajuste> toAjustesDomain(List<AjusteEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(e -> Ajuste.builder()
                        .id(e.getId())
                        .tipo(e.getTipo())
                        .monto(e.getMonto())
                        .razon(e.getRazon())
                        .build())
                .toList();
    }

    // ── Domain → DTO ─────────────────────────────────────────────────────────

    public LiquidacionListItemDTO toListItem(Liquidacion liq) {
        Ruta ruta = liq.getRuta();
        return new LiquidacionListItemDTO(
                liq.getId(),
                liq.getIdRuta(),
                ruta != null ? ruta.getFechaInicio() : null,
                ruta != null ? ruta.getFechaCierre() : null,
                ruta != null ? ruta.getTipoVehiculo() : null,
                ruta != null ? ruta.getPrecioParada() : null,
                ruta != null ? ruta.getNumeroParadas() : null,
                liq.getMontoBruto(),
                liq.getMontoNeto(),
                liq.getEstadoLiquidacion() != null ? liq.getEstadoLiquidacion().name() : null,
                liq.getFechaCalculo(),
                toAjustesDTO(liq.getAjustes())
        );
    }

    public LiquidacionDetalleDTO toDetalle(Liquidacion liq) {
        Ruta ruta = liq.getRuta();
        return new LiquidacionDetalleDTO(
                liq.getId(),
                liq.getIdContrato(),
                liq.getIdRuta(),
                ruta != null ? ruta.getFechaInicio() : null,
                ruta != null ? ruta.getFechaCierre() : null,
                ruta != null ? ruta.getTipoVehiculo() : null,
                ruta != null ? ruta.getPrecioParada() : null,
                ruta != null ? ruta.getNumeroParadas() : null,
                liq.getMontoBruto(),
                liq.getMontoNeto(),
                liq.getEstadoLiquidacion() != null ? liq.getEstadoLiquidacion().name() : null,
                liq.getFechaCalculo(),
                liq.getUsuarioId(),
                toAjustesDTO(liq.getAjustes())
        );
    }

    private List<AjusteLiquidacionDTO> toAjustesDTO(List<Ajuste> ajustes) {
        if (ajustes == null) return List.of();
        return ajustes.stream()
                .map(a -> new AjusteLiquidacionDTO(a.getId(), a.getTipo(), a.getMonto(), a.getRazon()))
                .toList();
    }
}
