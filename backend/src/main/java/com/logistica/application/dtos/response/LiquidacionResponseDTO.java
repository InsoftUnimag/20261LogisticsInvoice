package com.logistica.application.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class LiquidacionResponseDTO {

    private final UUID id;
    private final UUID idRuta;
    private final String estado;
    private final BigDecimal valorFinal;
    private final OffsetDateTime fechaCalculo;
    private final List<AjusteResponseDTO> ajustes;
}
