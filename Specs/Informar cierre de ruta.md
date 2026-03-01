# Feature Specification: Informar cierre de ruta

**Created**: 21/02/2026  

## User Scenarios & Testing *(mandatory)*

Dado un cierre de ruta informado por el módulo de rutas y flotas, el sistema debe registrar el evento y permitir su consulta en el módulo correspondiente.

 

### User Story 1 - Registrar cierre de ruta automáticamente (Priority: P1)

Como módulo de gestión de rutas, quiero que el sistema registre automáticamente el cierre de una ruta para garantizar la consistencia de la información y su disponibilidad para consulta.


**Why this priority**: El cierre de ruta habilita los procesos posteriores del sistema como cálculo de liquidación.

**Independent Test**: Enviar un evento de cierre de ruta desde el módulo de Rutas y Flotas y verificar que el sistema lo registre correctamente en la base de datos, dejándolo disponible para consulta.

**Acceptance Scenarios**:

1. **Scenario**:registrar ruta cerrada de manera exitosa
   - **Given** Ruta existente activa.
   - **When** El modulo de rutas y paquetes envía un informe de cierre de ruta.
   - **Then**  El sistema registra el evento de cierre de ruta y lo deja en el sistema para visualizar.

2. **Scenario**: Evento de ruta cerrada duplicado
   - **Given** Una ruta cerrada
   - **When** El modulo de rutas y paquetes envía nuevamente un informe de cierre de ruta.
   - **Then** El sistema rechaza el registro duplicado.

---

### Edge Cases



- What happens when se recibe un evento de cierre para una ruta inexistente?
- How does system handle Debe rechazar el evento y registrar un error?

- What happens when la base de datos no está disponible en el momento de registrar el cierre?
- How does system handle el sistema debe generar un fallo técnico y mantener el evento en cola?
## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST registrar automáticamente un informe de cierre de ruta cuando lo reciba del modulo de rutas y flotas.
- **FR-002**: System MUST Validar que la ruta este en estado “Activa” antes de permiter su cierre. 
- **FR-003**: Users MUST be able to almacenar fechas y horas del cierre de las rutas.
- **FR-004**: System MUST evitar registros duplicados de cierre para la misma ruta.
- **FR-005**: System MUST permitir la consulta de rutas cerradas desde le modulo correspondiente



### Key Entities 

- **[Ruta]**: Representa una ruta operativa del sistema. (idRuta, estado, fechaCreacion, fechaCierre )
- **[EventeCierreRuta]**: Representa el evento recibido desde el módulo externo. (idEventoCierre, idRuta, fechaEvento)
- **[Parada]**:  Representa los diferentes puntos donde se para el vehiculo para entregar un paquete. (idRuta, nombre)
## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los eventos válidos de cierre de ruta son registrados correctamente.
- **SC-002**: El sistema Evita el 100% de los registros duplicados
- **SC-003**: Se reduce a 0 los incidentes por inconsistencias de estados de rutas cerradas.
