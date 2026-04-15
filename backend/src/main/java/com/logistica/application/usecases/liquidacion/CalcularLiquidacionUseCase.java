package com.logistica.application.usecases.liquidacion;

import com.logistica.domain.exceptions.ContratoNotFoundException;
import com.logistica.domain.exceptions.LiquidacionDuplicadaException;
import com.logistica.domain.models.AuditoriaLiquidacion;
import com.logistica.domain.models.Contrato;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.models.Ruta;
import com.logistica.domain.repositories.AuditoriaLiquidacionRepository;
import com.logistica.domain.repositories.ContratoRepository;
import com.logistica.domain.repositories.LiquidacionRepository;
import com.logistica.domain.strategies.LiquidacionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalcularLiquidacionUseCase {

    private final LiquidacionRepository liquidacionRepository;
    private final ContratoRepository contratoRepository;
    private final AuditoriaLiquidacionRepository auditoriaRepository;
    private final LiquidacionStrategyFactory strategyFactory;

    @Transactional
    public Liquidacion execute(Ruta ruta, UUID idContrato) {
        // 1. Validar fechas de la ruta
        if (ruta.getFechaInicio() != null && ruta.getFechaCierre() != null && ruta.getFechaCierre().isBefore(ruta.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la fecha de inicio de la ruta.");
        }

        // 2. Validar que no exista una liquidación duplicada
        if (liquidacionRepository.existsByIdRuta(ruta.getId())) {
            throw new LiquidacionDuplicadaException("Ya existe una liquidación para la ruta con ID: " + ruta.getId());
        }

        // 3. Obtener el contrato desde la base de datos
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new ContratoNotFoundException("No se encontró el contrato con ID: " + idContrato));

        // 4. Seleccionar la estrategia y calcular el valor base
        LiquidacionStrategy strategy = strategyFactory.getStrategy(contrato.getTipoContratacion());
        BigDecimal valorBase = strategy.calcular(ruta, contrato);

        // 5. Crear la liquidación usando la regla de negocio del modelo
        Liquidacion liquidacion = Liquidacion.crear(ruta.getId(), contrato.getId(), valorBase);
        
        // El repositorio se encargará de guardar la liquidación y sus relaciones
        Liquidacion savedLiquidacion = liquidacionRepository.save(liquidacion);

        // 6. Registrar auditoría inicial
        AuditoriaLiquidacion auditoria = AuditoriaLiquidacion.crearCalculo(savedLiquidacion.getId(), valorBase);
        auditoriaRepository.save(auditoria);

        return savedLiquidacion;
    }
}
