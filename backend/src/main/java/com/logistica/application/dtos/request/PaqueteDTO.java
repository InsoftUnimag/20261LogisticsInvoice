package com.logistica.application.dtos.request;

import lombok.Data;
import java.util.UUID;

@Data
public class PaqueteDTO {

    @NotNull
    private UUID id;

    @NotNull
    private EstadoPaquete estadoFinal;

    private String novedades;
}