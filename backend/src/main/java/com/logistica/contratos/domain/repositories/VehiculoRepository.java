package com.logistica.contratos.domain.repositories;

import com.logistica.contratos.domain.models.Vehiculo;

import java.util.Optional;

public interface VehiculoRepository {
    Vehiculo guardar(Vehiculo vehiculo);
    Optional<Vehiculo> buscarPorId(Long id);
}
