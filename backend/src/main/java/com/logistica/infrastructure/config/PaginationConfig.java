package com.logistica.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class PaginationConfig {
    // Paginacion por defecto: page=0, size=10, sort=fechaCalculo DESC (configurado en el controlador)
}
