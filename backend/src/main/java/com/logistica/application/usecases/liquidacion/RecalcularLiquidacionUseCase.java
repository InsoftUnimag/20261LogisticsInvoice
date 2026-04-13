package com.logistica.application.usecases.liquidacion;

import com.logistica.domain.exceptions.LiquidacionNotFoundException;
import com.logistica.domain.exceptions.SolicitudRevisionNoAceptadaException;
import com.logistica.domain.models.Ajuste;
import com.logistica.domain.models.AuditoriaLiquidacion;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.repositories.AjusteRepository;
import com.logistica.domain.repositories.AuditoriaLiquidacionRepository;
import com.logistica.domain.repositories.LiquidacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RecalcularLiquidacionUseCase {

    private final LiquidacionRepository liquidacionRepository;
    private final AjusteRepository ajusteRepository;
    private final AuditoriaLiquidacionRepository auditoriaRepository;

    public RecalcularLiquidacionUseCase(LiquidacionRepository liquidacionRepository, AjusteRepository ajusteRepository, AuditoriaLiquidacionRepository auditoriaRepository) {
        this.liquidacionRepository = liquidacionRepository;
        this.ajusteRepository = ajusteRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional
    public Liquidacion execute(UUID liquidacionId, List<Ajuste> nuevosAjustes, String responsable) {
        // 1. Buscar la liquidación
        Liquidacion liquidacion = liquidacionRepository.findById(liquidacionId)
                .orElseThrow(() -> new LiquidacionNotFoundException("No se encontró la liquidación con ID: " + liquidacionId));

        // 2. Validar que la solicitud de revisión haya sido aceptada
        if (!liquidacion.isSolicitudRevisionAceptada()) {
            throw new SolicitudRevisionNoAceptadaException("No es posible recalcular. La solicitud de revisión para la liquidación " + liquidacionId + " no ha sido aceptada.");
        }

        // 3. Aplicar los nuevos ajustes
        BigDecimal valorAnterior = liquidacion.getValorFinal();
        BigDecimal totalNuevosAjustes = nuevosAjustes.stream()
                .map(Ajuste::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorNuevo = liquidacion.getValorFinal().add(totalNuevosAjustes);
        liquidacion.setValorFinal(valorNuevo);
        liquidacion.setEstado("RECALCULADA");

        // 4. Guardar los cambios
        ajusteRepository.saveAll(nuevosAjustes);
        Liquidacion liquidacionActualizada = liquidacionRepository.save(liquidacion);

        // 5. Registrar la auditoría
        AuditoriaLiquidacion auditoria = new AuditoriaLiquidacion(
                UUID.randomUUID(),
                liquidacionId,
                "RECALCULO",
                valorAnterior,
                valorNuevo,
                OffsetDateTime.now(),
                responsable
        );
        auditoriaRepository.save(auditoria);

        return liquidacionActualizada;
    }
}
