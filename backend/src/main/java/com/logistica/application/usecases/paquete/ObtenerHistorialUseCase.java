package com.logistica.application.usecases.paquete;

import com.logistica.application.dtos.response.HistorialEstadoDTO;
import com.logistica.domain.models.HistorialEstado;
import com.logistica.domain.repositories.HistorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObtenerHistorialUseCase {

    private final HistorialRepository historialRepository;

    public List<HistorialEstadoDTO> execute(Long idPaquete, int page, int size) {
        return historialRepository.findByIdPaquete(idPaquete, page, size)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private HistorialEstadoDTO toDto(HistorialEstado historial) {
        return new HistorialEstadoDTO(
                historial.getId(),
                historial.getIdPaquete(),
                historial.getEstado(),
                historial.getFecha()
        );
    }
}