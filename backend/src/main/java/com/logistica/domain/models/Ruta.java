package com.logistica.domain.models;

import lombok.*;


import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruta {
    private UUID id;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaCierre;
    private List<Paquete> paquetes;
}
