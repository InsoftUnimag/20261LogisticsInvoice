package com.logistica.contratos.application.usecases.contrato;

import com.logistica.contratos.application.dtos.request.ContratoRequestDTO;
import com.logistica.contratos.application.dtos.response.ContratoResponseDTO;
import com.logistica.contratos.domain.exceptions.ContratoYaExisteException;
import com.logistica.contratos.domain.models.Contrato;
import com.logistica.contratos.domain.models.Seguro;
import com.logistica.domain.models.Usuario;
import com.logistica.contratos.domain.models.Vehiculo;
import com.logistica.contratos.domain.repositories.ContratoRepository;
import com.logistica.contratos.domain.repositories.SeguroRepository;
import com.logistica.contratos.domain.repositories.UsuarioRepository;
import com.logistica.contratos.domain.repositories.VehiculoRepository;
import com.logistica.contratos.infrastructure.adapters.ContratoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearContratoUseCase {

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final SeguroRepository seguroRepository;
    private final ContratoMapper contratoMapper;

    @Transactional
    public ContratoResponseDTO ejecutar(ContratoRequestDTO dto) {
        if (contratoRepository.existePorIdContrato(dto.getIdContrato())) {
            throw new ContratoYaExisteException(dto.getIdContrato());
        }

        Usuario usuario = usuarioRepository.guardar(
                Usuario.builder().nombre(dto.getNombreConductor()).build()
        );

        Vehiculo vehiculo = vehiculoRepository.guardar(
                Vehiculo.builder().idUsuario(usuario.getIdUsuario()).tipo(dto.getTipoVehiculo()).build()
        );

        seguroRepository.guardar(
                Seguro.builder().idUsuario(usuario.getIdUsuario()).estado(dto.getEstadoSeguro()).build()
        );

        Contrato contrato = contratoMapper.toDomain(dto, usuario.getIdUsuario(), vehiculo.getIdVehiculo());
        Contrato guardado = contratoRepository.guardar(contrato);

        return contratoMapper.toResponseDTO(guardado);
    }
}
