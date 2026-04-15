package com.logistica.application.dtos.request;

import java.util.List;

@Data
public class RecalcularLiquidacionRequestDTO {

    @NotEmpty
    private List<AjusteDTO> ajustes;

    @NotBlank
    private String responsable;
}
