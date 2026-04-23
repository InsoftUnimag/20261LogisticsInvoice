package com.logistica.infrastructure.web.controllers;

import com.logistica.application.dtos.response.AjusteLiquidacionDTO;
import com.logistica.application.dtos.response.LiquidacionDetalleDTO;
import com.logistica.application.dtos.response.LiquidacionListItemDTO;
import com.logistica.application.dtos.response.LiquidacionListResponseDTO;
import com.logistica.application.usecases.liquidacion.BuscarLiquidacionesUseCase;
import com.logistica.application.usecases.liquidacion.ListarLiquidacionesUseCase;
import com.logistica.application.usecases.liquidacion.ObtenerDetalleLiquidacionUseCase;
import com.logistica.domain.exceptions.LiquidacionNoEncontradaException;
import com.logistica.infrastructure.config.SecurityConfig;
import com.logistica.infrastructure.security.JwtAuthenticationFilter;
import com.logistica.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// T012 — respuesta del listado incluye todos los campos de negocio del spec
// T013 — detalle retorna campos completos de trazabilidad
// T015 — liquidacion inexistente retorna HTTP 404 con codigo controlado
// T019 — falla del almacenamiento retorna HTTP 503 con mensaje funcional
@WebMvcTest(LiquidacionController.class)
@Import(SecurityConfig.class)
class LiquidacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListarLiquidacionesUseCase listarUseCase;

    @MockBean
    private ObtenerDetalleLiquidacionUseCase obtenerDetalleUseCase;

    @MockBean
    private BuscarLiquidacionesUseCase buscarUseCase;

    // Mockeamos el filtro para que no intente validar JWT en tests de controlador
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    // T012: la respuesta del listado contiene todos los campos requeridos por el spec
    @Test
    @WithMockUser(roles = "GESTOR_FINANCIERO")
    void dadoUsuarioAutorizado_cuandoListar_respuestaIncluyeTodosLosCamposRequeridos() throws Exception {
        UUID idLiq = UUID.randomUUID();
        UUID idRuta = UUID.randomUUID();

        AjusteLiquidacionDTO ajuste = new AjusteLiquidacionDTO(
                UUID.randomUUID(), "PENALIZACION", new BigDecimal("3000"), "Entrega tardia");

        LiquidacionListItemDTO item = new LiquidacionListItemDTO(
                idLiq, idRuta,
                LocalDateTime.of(2026, 1, 5, 8, 0),
                LocalDateTime.of(2026, 1, 5, 18, 0),
                "CAMION", new BigDecimal("15000"), 5,
                new BigDecimal("75000"), new BigDecimal("72000"),
                "CALCULADA", LocalDateTime.of(2026, 1, 6, 10, 0),
                List.of(ajuste));

        when(listarUseCase.ejecutar(any(), any()))
                .thenReturn(new LiquidacionListResponseDTO(List.of(item), 0, 10, 1, 1, true));

        mockMvc.perform(get("/api/liquidaciones").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido", hasSize(1)))
                .andExpect(jsonPath("$.contenido[0].id_liquidacion").value(idLiq.toString()))
                .andExpect(jsonPath("$.contenido[0].id_ruta").value(idRuta.toString()))
                .andExpect(jsonPath("$.contenido[0].tipo_vehiculo").value("CAMION"))
                .andExpect(jsonPath("$.contenido[0].precio_parada").value(15000))
                .andExpect(jsonPath("$.contenido[0].numero_paradas").value(5))
                .andExpect(jsonPath("$.contenido[0].monto_bruto").value(75000))
                .andExpect(jsonPath("$.contenido[0].monto_neto").value(72000))
                .andExpect(jsonPath("$.contenido[0].estado_liquidacion").value("CALCULADA"))
                .andExpect(jsonPath("$.contenido[0].ajustes", hasSize(1)))
                .andExpect(jsonPath("$.contenido[0].ajustes[0].tipo").value("PENALIZACION"))
                .andExpect(jsonPath("$.contenido[0].ajustes[0].monto").value(3000))
                .andExpect(jsonPath("$.total_elementos").value(1))
                .andExpect(jsonPath("$.es_ultima").value(true));
    }

    // T013: el detalle de una liquidacion retorna todos los campos de trazabilidad
    @Test
    @WithMockUser(roles = "GESTOR_FINANCIERO")
    void dadoIdValido_cuandoObtenerDetalle_retornaDetalleCompleto() throws Exception {
        UUID id = UUID.randomUUID();

        LiquidacionDetalleDTO detalle = new LiquidacionDetalleDTO(
                id, UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.of(2026, 2, 1, 7, 0),
                LocalDateTime.of(2026, 2, 1, 17, 0),
                "FURGON", new BigDecimal("12000"), 8,
                new BigDecimal("96000"), new BigDecimal("90000"),
                "CALCULADA", LocalDateTime.of(2026, 2, 2, 9, 0),
                "transportista-789", List.of());

        when(obtenerDetalleUseCase.ejecutar(any(UUID.class), any())).thenReturn(detalle);

        mockMvc.perform(get("/api/liquidaciones/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_liquidacion").value(id.toString()))
                .andExpect(jsonPath("$.tipo_vehiculo").value("FURGON"))
                .andExpect(jsonPath("$.numero_paradas").value(8))
                .andExpect(jsonPath("$.monto_bruto").value(96000))
                .andExpect(jsonPath("$.monto_neto").value(90000))
                .andExpect(jsonPath("$.estado_liquidacion").value("CALCULADA"))
                .andExpect(jsonPath("$.usuario_id").value("transportista-789"));
    }

    // T015: liquidacion inexistente retorna HTTP 404 con codigo LIQUIDACION_NO_ENCONTRADA
    @Test
    @WithMockUser(roles = "GESTOR_FINANCIERO")
    void dadoIdInexistente_cuandoObtenerDetalle_retornaHttp404ConCodigoControlado() throws Exception {
        UUID idInexistente = UUID.randomUUID();
        when(obtenerDetalleUseCase.ejecutar(any(UUID.class), any()))
                .thenThrow(new LiquidacionNoEncontradaException(
                        "La liquidacion con id " + idInexistente + " no existe en el registro."));

        mockMvc.perform(get("/api/liquidaciones/{id}", idInexistente).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("LIQUIDACION_NO_ENCONTRADA"))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // T019: falla del sistema de almacenamiento retorna HTTP 503 con mensaje funcional
    @Test
    @WithMockUser(roles = "GESTOR_FINANCIERO")
    void cuandoFallaAlmacenamiento_cuandoListar_retornaHttp503ConMensajeFuncional() throws Exception {
        when(listarUseCase.ejecutar(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("Connection refused"));

        mockMvc.perform(get("/api/liquidaciones").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.codigo").value("SISTEMA_NO_DISPONIBLE"))
                .andExpect(jsonPath("$.mensaje").value(containsString("almacenamiento")));
    }

    // T017 (seguridad): peticion sin autenticacion retorna HTTP 403
    @Test
    void sinAutenticacion_cuandoListar_retornaAccesoDenegado() throws Exception {
        mockMvc.perform(get("/api/liquidaciones").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
