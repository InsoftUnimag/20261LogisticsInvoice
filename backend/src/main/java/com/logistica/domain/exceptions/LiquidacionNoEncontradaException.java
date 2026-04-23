package com.logistica.domain.exceptions;

public class LiquidacionNoEncontradaException extends RuntimeException {
    public LiquidacionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
