package com.logistica.infrastructure.web.controllers;

import com.logistica.application.usecases.pago.ConsultarEstadoPagoUseCase;
import com.logistica.application.dtos.response.EstadoPagoResponseDTO;
import com.logistica.domain.enums.EstadoPagoEnum;
import com.logistica.domain.exceptions.PagoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultarEstadoPagoUseCase consultarEstadoPagoUseCase;

    @Test
    void obtenerEstadoPago_CuandoPagoExiste_DebeRetornar200OK() throws Exception {
        // Arrange
        UUID pagoId = UUID.randomUUID();
        EstadoPagoResponseDTO responseDTO = new EstadoPagoResponseDTO(
                pagoId,
                EstadoPagoEnum.PAGADO.name(),
                LocalDateTime.now(),
                new BigDecimal("1000.00"),
                null,
                UUID.randomUUID()
        );
        when(consultarEstadoPagoUseCase.ejecutar(pagoId)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/pagos/{id}", pagoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagoId").value(pagoId.toString()))
                .andExpect(jsonPath("$.estado").value("PAGADO"));
    }

    @Test
    void obtenerEstadoPago_CuandoPagoNoExiste_DebeRetornar404NotFound() throws Exception {
        // Arrange
        UUID pagoId = UUID.randomUUID();
        when(consultarEstadoPagoUseCase.ejecutar(pagoId)).thenThrow(new PagoNoEncontradoException("Pago no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/pagos/{id}", pagoId))
                .andExpect(status().isNotFound());
    }
}
