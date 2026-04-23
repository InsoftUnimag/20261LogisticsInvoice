package com.logistica.application.usecases.liquidacion;

import com.logistica.application.dtos.request.FiltroLiquidacionDTO;
import com.logistica.application.dtos.response.LiquidacionDetalleDTO;
import com.logistica.application.security.UsuarioAutenticado;
import com.logistica.domain.exceptions.AccesoDenegadoException;
import com.logistica.domain.exceptions.LiquidacionAunNoCalculadaException;
import com.logistica.domain.exceptions.LiquidacionNoEncontradaException;
import com.logistica.domain.models.Liquidacion;
import com.logistica.domain.repositories.LiquidacionRepository;
import com.logistica.infrastructure.adapters.LiquidacionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarLiquidacionesUseCase {

    private final LiquidacionRepository repository;
    private final LiquidacionMapper mapper;

    public BuscarLiquidacionesUseCase(LiquidacionRepository repository, LiquidacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public LiquidacionDetalleDTO ejecutar(FiltroLiquidacionDTO filtro, UsuarioAutenticado usuario) {
        Liquidacion liquidacion = resolverLiquidacion(filtro);
        validarAcceso(liquidacion, usuario);
        return mapper.toDetalle(liquidacion);
    }

    private Liquidacion resolverLiquidacion(FiltroLiquidacionDTO filtro) {
        if (filtro.getIdLiquidacion() != null) {
            return repository.buscarPorId(filtro.getIdLiquidacion())
                    .orElseThrow(() -> new LiquidacionNoEncontradaException(
                            "La liquidacion con id " + filtro.getIdLiquidacion() + " no existe en el registro."));
        }

        // Busqueda por idRuta: diferencia si la ruta existe pero aun no tiene liquidacion (escenario 4)
        return repository.buscarPorIdRuta(filtro.getIdRuta())
                .orElseThrow(() -> {
                    if (repository.existeRuta(filtro.getIdRuta())) {
                        return new LiquidacionAunNoCalculadaException(
                                "La ruta con id " + filtro.getIdRuta()
                                        + " aun no posee una liquidacion calculada.");
                    }
                    return new LiquidacionNoEncontradaException(
                            "No existe ninguna liquidacion asociada al id de ruta " + filtro.getIdRuta() + ".");
                });
    }

    private void validarAcceso(Liquidacion liquidacion, UsuarioAutenticado usuario) {
        if (!usuario.tienePermisoGlobal()
                && !liquidacion.getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new AccesoDenegadoException(
                    "No tiene permisos para visualizar esta liquidacion.");
        }
    }
}
