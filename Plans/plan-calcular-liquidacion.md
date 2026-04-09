# Implementation Plan: Calcular liquidación

**Date**: 2026-04-07
**Spec**: [Calcular liquidación.md]

## Summary

El objetivo de esta funcionalidad es automatizar el cálculo de las liquidaciones de los transportistas basándose en el cierre de rutas, el estado final de los paquetes y los modelos de contratación definidos. El sistema debe procesar datos de rutas finalizadas, aplicar tarifas, gestionar penalizaciones y permitir recálculos si existen ajustes posteriores, garantizando siempre la trazabilidad mediante auditorías.

## Technical Context

**Language/Version**: Java 21 / JavaScript / React 18+

**Primary Dependencies**: Spring Boot, PostgreSQL Driver, Axios

**Storage**: PostgreSQL 15


**Testing**: JUnit 5, Mockito / Jest

**Target Platform**: AWS

**Project Type**: Web application

**Data Integrity**: Uso de BigDecimal para todos los cálculos monetarios.

**Scheme Management**: Flyway para migraciones de PostgreSQL

**Security**: Spring Security + JWT para protección de endpoints financieros.

**API Pattern**: Implementacion de DTOs para desacoplar la base de datos de la capa de presentación.

**Performance Goals**: Procesamiento del cálculo en el servidor < 300ms

**Constraints**: Consistencia transaccional ACID, gestión segura de variables de entorno para la nube, y prevención de duplicados.

**Scale/Scope**: Preparado para escalar horizontalmente en la nube gestionando miles de cierres de ruta.

## Project Structure

### Documentation (this feature)

```text
specs/calcular-liquidacion/
├── plan.md              # Este archivo 
└── spec.md             # Especificación: Calcular liquidación.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/logistica/
│   ├── config/          # Configuraciones 
│   ├── controllers/     # Endpoints REST API
│   ├── models/          # Entidades JPA 
│   ├── repositories/    # Interfaces Spring Data JPA
│   └── services/        # Lógica de cálculo y reglas de negocio
├── Dockerfile           # Instrucciones para empaquetar en AWS
└── pom.xml / build.gradle

frontend/
├── src/
│   ├── components/      # UI: Tablas, modales y botones en React
│   ├── services/        # Peticiones Axios hacia la API de Spring Boot
│   └── pages/           # Vistas principales
├── Dockerfile           
└── package.json
```

**Structure Decision**: Se utiliza una estructura completamente desacoplada con archivos de configuración para contenedores, lo cual es el estándar de la industria para despliegues en AWS.

---

## Phase 1: Setup & DevOps Foundation (Shared Infrastructure)

**Purpose**: Configuración inicial y preparación para la nube.

- [ ] T001 Inicializar Spring Boot(Web, JPA, Flyway, Validation, Security)y el driver de PostgreSQL.
- [ ] T002 Inicializar React con Vite y configurara Axios Interceptors para manejo de errores globales.
- [ ] T003 Crear Docker Compose para entorno local (App + DB) y configurar Dockerfiles para AWS.
- [ ] T004 Definir el esquema inicial en un script de Flyway (V1__init_schema.sql).

---

## Phase 2: Foundational & Data Integrity (Blocking Prerequisites)

**Purpose**: Esquema de datos, conectividad y seguridad de comunicación.

- [ ] T005 Configurar CORS y un SecurityConfig basico (permitir/restringir rutas según roles) para permitir que el frontend de React se comunique con la API.
- [ ] T006 Crear las entidades JPA y sus respectivos DTOs para Contrato, Ruta, Liquidacion y Penalizacion mapeando a las tablas de PostgreSQL.
- [ ] T007 Implementar los JpaRepository para cada entidad.
- [ ] T008 Implementar un @RestControllerAdvice para capturar errores de base de datos y validaciones, retornando JSONs limpios a React.
- [ ] T009 Configurar un GlobalExceptionHandler que capture errores de validacion o EntityNotFoundException y devuelva mensajes legibles.
- 
**Checkpoint**: Backend expone rutas y se conecta a PostgreSQL mediante variables de entorno; el frontend puede hacer llamadas básicas sin errores de CORS.

---

## Phase 3: User Story 1 - Calcular liquidación automáticamente (Priority: P1)

**Goal**: Ejecutar el moyos de cálculo con precision monetaria y validación de estado de la liquidación basado en la información de la ruta.

**Independent Test**: Lanzar el backend y enviar un POST con Postman o URL simulando a React. Verificar el cálculo y la inserción en PostgreSQL.

### Tests for User Story 1

- [ ] T010 [P] [US1] JUnit 5 test para validar que CalculationService aplique el calculo correcto según el modelo de contratación.
- [ ] T011 [P] [US1] Test de integración con @DataJpaTest para confirmar la restricción de liquidaciones duplicadas.

### Implementation for User Story 1

- [ ] T012 [P] [US1] Implementar el motor de   cálculo y penalizaciones base en CalculationService.java. Utilizar el patrón Strategy por tipo de contrato.
- [ ] T013 [US1] Asegurar que el método de creación de liquidación sea @transactional para evitar estados incosistentes
- [ ] T014 [US1] Crear el controlador REST POST /api/liquidaciones/calcular.
- [ ] T015 [US1] Desarrollar el servicio en React con Axios para invocar el endpoint de cálculo.
- [ ] T016 [US1] Crear el componente visual en React que muestre el resumen del cálculo exitoso al usuario.

---

## Phase 4: User Story 2 - Recalcular liquidación (Priority: P2)

**Goal**: Permitir agregar nuevos ajustes sobre un cálculo existente manteniendo la trazabilidad.

**Independent Test**: Modificar penalizaciones desde la UI de React, pulsar recalcular, y confirmar en la base de datos que el registro de auditoría fue creado en PostgreSQL.

### Tests for User Story 2

- [ ] T017 [P] [US2] Test unitario para verificar que al actualizar una liquidación, el registro previo se guarde íntegro en la tabla de auditoría.
- [ ] T018 [US2] Test de componente en React para confirmar que la vista de liquidación se actualiza sin recargar la página completa.

### Implementation for User Story 2

- [ ] T019 [P] [US2] Crear la entidad JPA AuditoriaLiquidacion para cumplir con el FR-003.
- [ ] T020 [US2] Implementar el método de actualización en CalculationService y registrar la auditoría.
- [ ] T021 [US2] Crear el controlador PUT /api/liquidaciones/{id}/recalcular.
- [ ] T022 [US2] Desarrollar un formulario en React para ajustes manuales con campos de "Motivo del ajuste" (obligatorio para la auditoría).

---

## Phase N: Polish & Cross-Cutting Concerns

- [ ] T023 Configurar perfiles de Spring Boot específicos para despliegue en AWS RDS.
- [ ] T024 Añadir Swagger/OpenAPI para documentar la API y facilitar la integración con el equipo de frontend.
- [ ] T025 Implementar manejo de estados de carga en React mientras se espera la respuesta de Spring Boot.

---

## Dependencies & Execution Order

**Variables y Configuración (Fase 1 y 2)**: Es crítico resolver el CORS y la inyección de credenciales de DB antes de empezar a programar la lógica de negocio.

**Modelos JPA**: Se deben definir las relaciones en Java antes de codificar los repositorios.

**Servicios antes de Controladores**: El motor de cálculo matemático se programa y prueba con JUnit antes de exponerlo vía REST.

**Integración UI**: React entra en juego al final de cada historia de usuario, consumiendo lo que Spring Boot ya tiene validado.
