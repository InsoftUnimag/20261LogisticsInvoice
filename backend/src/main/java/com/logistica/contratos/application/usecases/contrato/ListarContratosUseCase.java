package com.logistica.contratos.application.usecases.contrato;

import com.logistica.contratos.application.dtos.response.ContratoResponseDTO;
import com.logistica.contratos.domain.repositories.ContratoRepository;
import com.logistica.contratos.infrastructure.adapters.ContratoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarContratosUseCase {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;

    @Transactional(readOnly = true)
    public List<ContratoResponseDTO> ejecutar() {
        return contratoRepository.listar().stream()
                .map(contratoMapper::toResponseDTO)
                .toList();
    }
}
