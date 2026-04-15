package com.logistica.domain.repositories;

import com.logistica.domain.models.AuditoriaLiquidacion;
import java.util.List;
import java.util.UUID;

public interface AuditoriaLiquidacionRepository {
    AuditoriaLiquidacion save(AuditoriaLiquidacion auditoriaLiquidacion);
    List<AuditoriaLiquidacion> findByIdLiquidacion(UUID liquidacionId);
}
