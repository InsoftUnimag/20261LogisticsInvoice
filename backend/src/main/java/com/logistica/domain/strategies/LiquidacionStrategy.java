package com.logistica.domain.strategies;

import com.logistica.domain.enums.TipoContratacion;
import com.logistica.domain.models.Contrato;
import com.logistica.domain.models.Ruta;

import java.math.BigDecimal;

public interface LiquidacionStrategy {

    TipoContratacion soporta();

    BigDecimal calcular(Ruta ruta, Contrato contrato);
}