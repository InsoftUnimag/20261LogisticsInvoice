package com.logistica.domain.models;

import com.logistica.domain.enums.EstadoProcesamiento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Ruta {
    private UUID rutaId;
    private Transportista transportista;
    private UUID vehiculoId;
    private String tipoVehiculo;
    private String modeloContrato;
    private LocalDateTime fechaInicioTransito;
    private LocalDateTime fechaCierre;
    private EstadoProcesamiento estadoProcesamiento;

    @Singular
    private List<Parada> paradas ;
    public Ruta conEstado(EstadoProcesamiento nuevoEstado) {
        return Ruta.builder()
                .rutaId(this.rutaId)
                .transportista(this.transportista)
                .vehiculoId(this.vehiculoId)
                .tipoVehiculo(this.tipoVehiculo)
                .modeloContrato(this.modeloContrato)
                .fechaInicioTransito(this.fechaInicioTransito)
                .fechaCierre(this.fechaCierre)
                .estadoProcesamiento(nuevoEstado)
                .paradas(this.paradas)
                .build();
    }




}
