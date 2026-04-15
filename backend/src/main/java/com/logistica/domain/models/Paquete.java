package com.logistica.domain.models;

import lombok.*;


import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paquete {
    private UUID id;
    private String estadoFinal; // Ej: "ENTREGADO", "FALLIDO_CLIENTE", "FALLIDO_TRANSPORTISTA"
    private String novedades;
}
