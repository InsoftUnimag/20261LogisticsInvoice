package com.logistica.contratos.application.mappers;

import com.logistica.contratos.application.dtos.response.ContratoResponseDTO;
import com.logistica.contratos.domain.models.Contrato;
import org.springframework.stereotype.Component;

@Component
public class ContratoResponseMapper {

    public ContratoResponseDTO toResponseDTO(Contrato contrato) {
        return ContratoResponseDTO.builder()
                .id(contrato.getId())
                .idContrato(contrato.getIdContrato())
                .tipoContrato(contrato.getTipoContrato())
                .nombreConductor(contrato.getNombreConductor())
                .precioParadas(contrato.getPrecioParadas())
                .precio(contrato.getPrecio())
                .tipoVehiculo(contrato.getTipoVehiculo())
                .fechaInicio(contrato.getFechaInicio())
                .fechaFinal(contrato.getFechaFinal())
                .estadoSeguro(contrato.getEstadoSeguro())
                .createdAt(contrato.getCreatedAt())
                .build();
    }
}
