package com.logistica.application.usecases.liquidacion;

import com.logistica.application.dtos.response.LiquidacionDetalleDTO;
import com.logistica.application.security.UsuarioAutenticado;
import com.logistica.domain.exceptions.AccesoDenegadoException;
import com.logistica.domain.exceptions.LiquidacionNoEncontradaException;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.repositories.LiquidacionRepository;
import com.logistica.infrastructure.adapters.LiquidacionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObtenerDetalleLiquidacionUseCase {

    private final LiquidacionRepository repository;
    private final LiquidacionMapper mapper;

    public ObtenerDetalleLiquidacionUseCase(LiquidacionRepository repository, LiquidacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public LiquidacionDetalleDTO ejecutar(UUID id, UsuarioAutenticado usuario) {
        Liquidacion liquidacion = repository.buscarPorId(id)
                .orElseThrow(() -> new LiquidacionNoEncontradaException(
                        "La liquidacion con id " + id + " no existe en el registro."));

        validarAcceso(liquidacion, usuario);

        return mapper.toDetalle(liquidacion);
    }

    private void validarAcceso(Liquidacion liquidacion, UsuarioAutenticado usuario) {
        if (!usuario.tienePermisoGlobal()
                && !liquidacion.getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new AccesoDenegadoException(
                    "No tiene permisos para visualizar esta liquidacion.");
        }
    }
}
