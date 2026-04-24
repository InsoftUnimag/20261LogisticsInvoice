package com.logistica.ruta.infrastructure.persistence.repositories;

import com.logistica.ruta.domain.enums.TipoVehiculo;
import com.logistica.ruta.domain.repositories.TarifaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TarifaRepositoryImpl implements TarifaRepository {

    @Override
    public boolean existeTarifaParaVehiculo(TipoVehiculo tipoVehiculo) {
        return tipoVehiculo != null;
    }
}
