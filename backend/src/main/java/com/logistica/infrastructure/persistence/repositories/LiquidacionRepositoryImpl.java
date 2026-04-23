package com.logistica.infrastructure.persistence.repositories;

import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.repositories.LiquidacionRepository;
import com.logistica.infrastructure.adapters.LiquidacionMapper;
import com.logistica.infrastructure.persistence.entities.LiquidacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class LiquidacionRepositoryImpl implements LiquidacionRepository {

    private final LiquidacionJpaRepository jpaRepository;
    private final RutaJpaRepository rutaJpaRepository;
    private final LiquidacionMapper mapper;

    public LiquidacionRepositoryImpl(LiquidacionJpaRepository jpaRepository,
                                     RutaJpaRepository rutaJpaRepository,
                                     LiquidacionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.rutaJpaRepository = rutaJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<Liquidacion> listarTodas(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Liquidacion> listarPorUsuario(String usuarioId, Pageable pageable) {
        return jpaRepository.findByUsuarioId(usuarioId, pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Liquidacion> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Liquidacion> buscarPorIdRuta(UUID idRuta) {
        return jpaRepository.findByRuta_Id(idRuta).map(mapper::toDomain);
    }

    @Override
    public boolean existeRuta(UUID idRuta) {
        return rutaJpaRepository.existsById(idRuta);
    }
}
