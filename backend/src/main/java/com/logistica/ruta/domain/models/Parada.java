package com.logistica.ruta.domain.models;

import com.logistica.ruta.domain.enums.EstadoParada;
import com.logistica.ruta.domain.enums.MotivoFalla;
import com.logistica.ruta.domain.enums.ResponsableFalla;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Parada {

    private UUID paradaId;
    private UUID paqueteId;
    private EstadoParada estado;
    private MotivoFalla motivoFalla;

    public  ResponsableFalla getResponsable(){
        return motivoFalla != null ? motivoFalla.getResponsable() : null;

    }

    public static   Parada crear( UUID paradaId, EstadoParada estado, MotivoFalla motivoFalla ){
        if (paradaId == null) {
            throw new IllegalArgumentException("paradaId no puede ser null");
        }

        if (estado == null) {
            throw new IllegalArgumentException("estado no puede ser null");
        }

        if (estado == EstadoParada.FALLIDA && motivoFalla == null) {
            throw new IllegalArgumentException(
                    "Parada fallida sin motivo. paradaId: " + paradaId
            );
        }
        return Parada.builder()
                .paradaId(paradaId)
                .estado(estado)
                .motivoFalla(motivoFalla)
                .build();
    }

}
