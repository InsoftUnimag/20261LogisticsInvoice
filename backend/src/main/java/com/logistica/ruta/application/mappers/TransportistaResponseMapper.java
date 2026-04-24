package com.logistica.ruta.application.mappers;

import com.logistica.ruta.application.dtos.response.TransportistaResponseDTO;
import com.logistica.ruta.domain.models.Transportista;
import org.springframework.stereotype.Component;

@Component
public class TransportistaResponseMapper {

    public TransportistaResponseDTO toResponse(Transportista t) {
        if (t == null) return null;

        return TransportistaResponseDTO.builder()
                .TransportistaId(t.getTransportistaId())
                .nombre(t.getNombre())
                .build();
    }
}