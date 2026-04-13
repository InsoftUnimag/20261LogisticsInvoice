package com.logistica.infrastructure.web.controllers;

import com.logistica.application.dtos.request.CierreRutaEventDTO;
import com.logistica.application.dtos.response.LiquidacionResponseDTO;
import com.logistica.application.usecases.liquidacion.CalcularLiquidacionUseCase;
import com.logistica.domain.models.Paquete;
import com.logistica.domain.models.Ruta;
import com.logistica.infrastructure.adapters.LiquidacionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos", description = "Endpoints para la recepción de eventos del sistema.")
public class EventoController {

    private final CalcularLiquidacionUseCase calcularLiquidacionUseCase;
    private final LiquidacionMapper liquidacionMapper;

    public EventoController(CalcularLiquidacionUseCase calcularLiquidacionUseCase, LiquidacionMapper liquidacionMapper) {
        this.calcularLiquidacionUseCase = calcularLiquidacionUseCase;
        this.liquidacionMapper = liquidacionMapper;
    }

    @PostMapping("/cierre-ruta")
    @Operation(summary = "Maneja el evento de cierre de ruta para calcular una liquidación.",
            description = "Este endpoint es invocado por un sistema externo (ej. Módulo de Rutas) para notificar que una ruta ha finalizado y que se debe proceder con el cálculo automático de la liquidación.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liquidación calculada exitosamente.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidacionResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "El contrato especificado no fue encontrado."),
                    @ApiResponse(responseCode = "409", description = "Ya existe una liquidación para la ruta especificada.")
            })
    public ResponseEntity<LiquidacionResponseDTO> handleCierreRuta(@RequestBody CierreRutaEventDTO event) {
        Ruta ruta = new Ruta(
                event.getIdRuta(),
                null,
                event.getFechaCierre(),
                event.getPaquetes().stream()
                        .map(p -> new Paquete(p.getId(), p.getEstadoFinal(), p.getNovedades()))
                        .collect(Collectors.toList())
        );

        com.logistica.domain.models.Liquidacion liquidacion = calcularLiquidacionUseCase.execute(ruta, event.getIdContrato());

        LiquidacionResponseDTO response = liquidacionMapper.toResponseDTO(liquidacion);

        return ResponseEntity.ok(response);
    }
}
