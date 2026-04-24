package com.logistica.contratos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class Seguro {
    private Long idSeguro;
    private Long idUsuario;
    private String estado;
}
