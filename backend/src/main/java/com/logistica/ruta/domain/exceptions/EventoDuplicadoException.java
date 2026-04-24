package com.logistica.ruta.domain.exceptions;

import java.util.UUID;

public class EventoDuplicadoException extends DomainException {
    private static final String MESSAGE = "Evento duplicado, ruta procesada: ";


    public EventoDuplicadoException(UUID rutaId) {
        super( MESSAGE + rutaId);
    }
}
