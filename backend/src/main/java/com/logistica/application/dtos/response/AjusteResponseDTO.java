package com.logistica.application.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AjusteResponseDTO {
    private final UUID id;
    private final String tipo;
    private final BigDecimal monto;
    private final String motivo;
}
