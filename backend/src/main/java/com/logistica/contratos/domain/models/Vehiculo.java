package com.logistica.contratos.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Vehiculo {
    private UUID idVehiculo;
    private Transportista transportistaId;
    private String tipo;
}
