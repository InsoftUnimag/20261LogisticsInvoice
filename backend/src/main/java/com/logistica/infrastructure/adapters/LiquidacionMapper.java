package com.logistica.infrastructure.adapters;

import com.logistica.application.dtos.response.LiquidacionResponseDTO;
import com.logistica.domain.models.Liquidacion;
import com.logistica.infrastructure.persistence.entities.LiquidacionEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LiquidacionMapper {

    private final AjusteMapper ajusteMapper;

    public LiquidacionMapper(AjusteMapper ajusteMapper) {
        this.ajusteMapper = ajusteMapper;
    }

    public LiquidacionEntity toEntity(Liquidacion model) {
        if (model == null) {
            return null;
        }

        LiquidacionEntity entity = new LiquidacionEntity();
        entity.setId(model.getId());
        entity.setIdRuta(model.getIdRuta());
        entity.setIdContrato(model.getIdContrato());
        entity.setEstado(model.getEstado());
        entity.setValorFinal(model.getValorFinal());
        entity.setFechaCalculo(model.getFechaCalculo());
        entity.setSolicitudRevisionAceptada(model.isSolicitudRevisionAceptada());
        if (model.getAjustes() != null) {
            entity.setAjustes(model.getAjustes().stream()
                    .map(ajusteMapper::toEntity)
                    .collect(Collectors.toList()));
            entity.getAjustes().forEach(ajusteEntity -> ajusteEntity.setLiquidacion(entity));
        }
        return entity;
    }

    public Liquidacion toModel(LiquidacionEntity entity) {
        if (entity == null) {
            return null;
        }

        Liquidacion model = new Liquidacion(
                entity.getId(),
                entity.getIdRuta(),
                entity.getIdContrato(),
                entity.getEstado(),
                entity.getValorFinal(),
                entity.getFechaCalculo(),
                entity.getAjustes() != null ? entity.getAjustes().stream()
                        .map(ajusteMapper::toModel)
                        .collect(Collectors.toList()) : null
        );
        model.setSolicitudRevisionAceptada(entity.isSolicitudRevisionAceptada());
        return model;
    }

    public LiquidacionResponseDTO toResponseDTO(Liquidacion model) {
        if (model == null) {
            return null;
        }

        LiquidacionResponseDTO dto = new LiquidacionResponseDTO();
        dto.setId(model.getId());
        dto.setIdRuta(model.getIdRuta());
        dto.setEstado(model.getEstado());
        dto.setValorFinal(model.getValorFinal());
        dto.setFechaCalculo(model.getFechaCalculo());
        // El campo solicitudRevisionAceptada no se expone en el DTO de respuesta por ahora
        if (model.getAjustes() != null) {
            dto.setAjustes(model.getAjustes().stream()
                    .map(ajusteMapper::toResponseDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
