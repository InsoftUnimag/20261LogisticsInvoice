package com.logistica.ruta.domain.repositories;

import com.logistica.ruta.domain.enums.TipoVehiculo;

public interface TarifaRepository {
    boolean existeTarifaParaVehiculo(TipoVehiculo tipoVehiculo);
}
