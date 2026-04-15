package com.logistica.application.dtos.request;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CierreRutaEventDTO {

    private UUID idRuta;

    @NotNull
    private UUID idContrato;

    @NotNull
    private OffsetDateTime fechaCierre;

    @NotEmpty
    private List<PaqueteDTO> paquetes;
}
