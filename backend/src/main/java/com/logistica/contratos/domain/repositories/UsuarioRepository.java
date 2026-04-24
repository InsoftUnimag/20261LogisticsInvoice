package com.logistica.contratos.domain.repositories;

import com.logistica.domain.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
}
