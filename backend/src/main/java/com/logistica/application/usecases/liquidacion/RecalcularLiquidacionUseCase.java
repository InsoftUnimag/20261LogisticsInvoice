package com.logistica.application.usecases.liquidacion;

import com.logistica.domain.exceptions.LiquidacionNotFoundException;
import com.logistica.domain.models.Ajuste;
import com.logistica.domain.models.AuditoriaLiquidacion;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.repositories.AjusteRepository;
import com.logistica.domain.repositories.AuditoriaLiquidacionRepository;
import com.logistica.domain.repositories.LiquidacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecalcularLiquidacionUseCase {

    private final LiquidacionRepository liquidacionRepository;
    private final AjusteRepository ajusteRepository;
    private final AuditoriaLiquidacionRepository auditoriaRepository;

    @Transactional
    public Liquidacion execute(UUID liquidacionId, List<Ajuste> nuevosAjustes, String responsable) {
        // 1. Buscar la liquidación
        Liquidacion liquidacion = liquidacionRepository.findById(liquidacionId)
                .orElseThrow(() -> new LiquidacionNotFoundException("No se encontró la liquidación con ID: " + liquidacionId));

        BigDecimal valorAnterior = liquidacion.getValorFinal();

        // 2. Usar la regla de negocio del modelo para recalcular
        // Nota: El valorBase se mantiene o se podría pasar uno nuevo si fuera necesario. 
        // Para este flujo asumimos que el base es el mismo y cambian los ajustes.
        liquidacion.recalcular(liquidacion.getValorBase(), nuevosAjustes);

        // 3. Guardar los cambios
        ajusteRepository.saveAll(nuevosAjustes);
        Liquidacion liquidacionActualizada = liquidacionRepository.save(liquidacion);

        // 4. Registrar la auditoría usando la factory del modelo
        AuditoriaLiquidacion auditoria = AuditoriaLiquidacion.crearRecalculo(
                liquidacionId,
                valorAnterior,
                liquidacionActualizada.getValorFinal(),
                responsable
        );
        auditoriaRepository.save(auditoria);

        return liquidacionActualizada;
    }
}
