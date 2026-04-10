# Implementation Plan: Visualizar estado del pago

**Date**: 2026-04-08
**Spec**: [Visualizar estado del pago.md]

## Summary

Este módulo está orientado al usuario final (el transportista o conductor) y permite la consulta segura del estado y los detalles de sus pagos. La implementación pone un fuerte énfasis en la seguridad (Autorización) para restringir el acceso a los datos de pagos estrictamente a sus propietarios. Adicionalmente, incluye la funcionalidad de generación dinámica de documentos en formato PDF para la descarga de comprobantes o vouchers de liquidación.

## Technical Context

**Language/Version**: Java 21 / JavaScript / React 18+
**Primary Dependencies**: Spring Boot (Web, Data JPA, Spring Security), OpenPDF (para la generación del voucher), Axios, FileSaver.js (React)
**Storage**: PostgreSQL 15
**Testing**: JUnit 5, Spring Security Test, Mockito / Jest
**Target Platform**: AWS
**Project Type**: Web application (Backend API + Frontend Dashboard del Transportista)
**Performance Goals**: Consultas de estado en < 200ms; Generación y descarga de PDF en < 1 segundo.
**Constraints**: Restricción absoluta de acceso basada en el ID del usuario autenticado (FR-005). Auditoría de seguridad para intentos de acceso no autorizados (Edge Case).

## Project Structure

### Documentation (this feature)

```text
specs/visualizar-pago/
├── plan.md              # Este archivo 
└── spec.md             # Especificación: Visualizar estado del pago.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/logistica/
│   ├── config/          # Configuración de Spring Security (JWT)
│   ├── controllers/     # Endpoints REST (GET /pagos)
│   ├── dtos/            # DTOs de respuesta (ResumenPagoDTO, DetallePagoDTO)
│   ├── models/          # Entidades JPA (Pago, EstadoPago, Usuario)
│   ├── repositories/    # Interfaces Spring Data JPA
│   ├── security/        # Filtros de seguridad e interceptores
│   ├── services/        # Lógica de consulta y validación de propiedad
│   └── utils/           # Generador de PDF (PdfVoucherGenerator.java)
└── src/test/java/       # Pruebas de autorización y generación de archivos

frontend/
├── src/
│   ├── components/      # UI: Tarjetas de estado de pago, Botón de descarga
│   ├── hooks/           # Manejo de blobs (archivos PDF) en React
│   ├── services/        # Peticiones autenticadas (Axios con JWT)
│   └── pages/           # Vistas: "Mis Pagos" y "Detalle del Pago"
└── package.json
```

**Structure Decision**: Se añade la capa de security/ y utils/ en el backend para manejar la autenticación y la construcción de archivos binarios, aislando estas responsabilidades de la lógica de negocio puramente financiera.

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Configurar las herramientas de seguridad y generación de documentos.

- [ ] T001 Añadir las dependencias spring-boot-starter-security y io.jsonwebtoken:jjwt (o similar para JWT) en el pom.xml/build.gradle.
- [ ] T002 Añadir la dependencia com.github.librepdf:openpdf (o iText) para la generación de archivos PDF en Java.
- [ ] T003 En React, instalar file-saver para manejar la descarga del blob del PDF de manera limpia en el navegador.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implementar la capa de seguridad y preparar las consultas optimizadas.

- [ ] T004 Configurar SecurityFilterChain en Spring Boot para exigir que todas las peticiones a /api/pagos/** requieran un token de autenticación válido.
- [ ] T005 Implementar la extracción del idUsuario directamente desde el token JWT en el contexto de seguridad de Spring (para no confiar en el ID enviado por el cliente en la URL).
- [ ] T006 Implementar consultas JPQL personalizadas en PagoRepository (ej. findByUsuario_IdUsuario(Long userId)) usando JOIN FETCH para traer el EstadoPago sin incurrir en problemas de rendimiento (N+1).

**Checkpoint**: El backend rechaza peticiones sin token y es capaz de identificar de manera segura quién está haciendo la solicitud.

---

## Phase 3: User Story 1 - Consultar estado del pago (Priority: P1)

**Goal**: Permitir al usuario ver una lista resumida de sus pagos y el estado actual de cada uno.

**Independent Test**: Realizar una petición GET con el token de "Usuario A". Verificar que la respuesta solo contenga los pagos del "Usuario A" y nunca los del "Usuario B".

### Tests for User Story 1

- [ ] T007 [P] [US1] Test de integración con @WithMockUser para validar que el endpoint retorna correctamente la lista de pagos del usuario autenticado.
- [ ] T008 [P] [US1] Test de seguridad verificando que un usuario sin autenticación reciba un HTTP 401 Unauthorized.

### Implementation for User Story 1

- [ ] T009 [P] [US1] Crear el ResumenPagoDTO (IdRuta, MontodePago, Fecha, Estado).
- [ ] T010 [US1] Implementar en PagoService la lógica para buscar los pagos utilizando el ID extraído del contexto de seguridad.
- [ ] T011 [US1] Crear el endpoint GET /api/pagos/mis-pagos.
- [ ] T012 [US1] Desarrollar la vista en React que consuma este endpoint y muestre una tabla o lista de tarjetas con etiquetas de colores según el estado (Verde = Pagado, Amarillo = Pendiente, Rojo = Rechazado).

---

## Phase 4: User Story 2 - Consultar detalle y Descargar Factura (Priority: P2)

**Goal**: Mostrar el desglose completo del pago y generar el PDF transaccional.

**Independent Test**: Intentar acceder al detalle del pago "X" (que pertenece a otro usuario). El sistema debe retornar HTTP 403 Forbidden y generar un log de seguridad. Descargar el comprobante y abrir el PDF localmente.

### Tests for User Story 2

- [ ] T013 [P] [US2] Test unitario para garantizar que la validación de propiedad bloquee el acceso cruzado (FR-005 / Edge Case).
- [ ] T014 [P] [US2] Test unitario para PdfVoucherGenerator verificando que el documento binario (byte array) se genera correctamente sin lanzar excepciones.

### Implementation for User Story 2

- [ ] T015 [P] [US2] Crear DetallePagoDTO que incluya las penalizaciones/ajustes, liquidación base y motivos de rechazo si aplica.
- [ ] T016 [US2] Implementar la validación estricta en PagoService: if (!pago.getUsuario().getId().equals(usuarioAutenticadoId)) throw new AccessDeniedException(). Adicionalmente, registrar este evento con un logger nivel WARN.
- [ ] T017 [US2] Crear el generador de PDF utilizando OpenPDF para maquetar la estructura de la factura (IDRuta, Fecha, MontoBase, Penalizaciones, MontoNeto).
- [ ] T018 [US2] Crear los endpoints GET /api/pagos/{id}/detalle y GET /api/pagos/{id}/voucher (este último retornando application/pdf).
- [ ] T019 [US2] En React, crear la vista de detalle y un botón "Descargar Comprobante" que maneje la respuesta como un Blob y fuerce la descarga en el navegador.

---

## Phase N: Polish & Cross-Cutting Concerns

- [ ] T020 Configurar logs de auditoría específicos (SLF4J) para monitorizar en AWS CloudWatch los intentos de acceso a recursos ajenos.
- [ ] T021 Optimizar el tamaño de la respuesta PDF y añadir metadatos (Autor, Fecha de creación) al archivo generado.
- [ ] T022 Diseñar una plantilla HTML/CSS para la generación del PDF si se decide usar un motor como Thymeleaf + Flying Saucer en lugar de código Java puro para una factura más estética.

---

## Dependencies & Execution Order

**Seguridad Primero**: Es inútil desarrollar los endpoints de consulta si el sistema no puede identificar quién está preguntando. La configuración del token JWT es el paso cero.

**Consultas Base (US1)**: Mapear la relación entre Pago, EstadoPago y Liquidación para que las vistas resumidas funcionen rápido.

**Validación de Propiedad (US2)**: Implementar el cerrojo lógico de seguridad antes de exponer los detalles profundos del pago.

**Generación de Archivos (US2)**: El PDF se genera como última etapa, utilizando los mismos datos que ya fueron extraídos y validados por el servicio de detalle del pago.
