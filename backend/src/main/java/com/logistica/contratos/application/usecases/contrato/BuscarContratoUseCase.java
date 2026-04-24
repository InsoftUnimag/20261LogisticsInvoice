package com.logistica.contratos.application.usecases.contrato;

import com.logistica.contratos.application.dtos.response.ContratoResponseDTO;
import com.logistica.contratos.domain.exceptions.RecursoNoEncontradoException;
import com.logistica.contratos.domain.repositories.ContratoRepository;
import com.logistica.contratos.infrastructure.adapters.ContratoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarContratoUseCase {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;

    @Transactional(readOnly = true)
    public ContratoResponseDTO ejecutar(String idContrato) {
        return contratoRepository.buscarPorIdContrato(idContrato)
                .map(contratoMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contrato no encontrado"));
    }
}
