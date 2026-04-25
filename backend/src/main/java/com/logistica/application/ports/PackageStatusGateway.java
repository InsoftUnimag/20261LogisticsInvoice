package com.logistica.application.ports;

import java.util.concurrent.CompletableFuture;

public interface PackageStatusGateway {

    CompletableFuture<PackageStatusResult> consultarEstado(Long idRuta, Long idPaquete);
}
