package com.logistica.ruta.application.mappers;

import com.logistica.ruta.application.dtos.request.ConductorEventDTO;
import com.logistica.ruta.domain.models.Transportista;
import org.springframework.stereotype.Component;

@Component
public class TransportistaEventMapper {

    public Transportista toDomain(ConductorEventDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Conductor es obligatorio en el evento");
        }

        if (dto.getConductorId() == null) {
            throw new IllegalArgumentException("conductorId es obligatorio");
        }

        return Transportista.builder()
                .transportistaId(dto.getConductorId())
                .nombre(dto.getNombre() != null ? dto.getNombre().trim() : null)
                .build();
    }
}