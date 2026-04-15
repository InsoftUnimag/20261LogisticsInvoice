package com.logistica.domain.models;

import lombok.*;


import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ajuste {

    private UUID id;
    private UUID idLiquidacion;
    private String tipo;
    private BigDecimal monto;
    private String motivo;
}
