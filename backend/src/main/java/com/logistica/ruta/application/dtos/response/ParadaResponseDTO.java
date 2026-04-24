package com.logistica.ruta.application.dtos.response;

import com.logistica.ruta.domain.enums.EstadoParada;
import com.logistica.ruta.domain.enums.MotivoFalla;
import com.logistica.ruta.domain.enums.ResponsableFalla;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ParadaResponseDTO {
    private UUID paradaId;
    private UUID paqueteId;
    private EstadoParada estado;
    private MotivoFalla motivoFalla;
    private ResponsableFalla responsable;
}
