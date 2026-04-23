package com.logistica.domain.exceptions;

public class LiquidacionAunNoCalculadaException extends RuntimeException {
    public LiquidacionAunNoCalculadaException(String mensaje) {
        super(mensaje);
    }
}
