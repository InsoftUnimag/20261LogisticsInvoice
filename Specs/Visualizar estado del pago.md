# Feature Specification: Visualizar estado del pago

**Created**: 24/02/2026  

## User Scenarios & Testing *(mandatory)*

Dado la visualización de un pago, sistema debe enseñar el estado del pago y los ajustes o penalizaciones dadas al pago.

### User Story 1 - Consultar estado del pago (Priority: P1)

Como usuario deseo visualizar el estado actual de mi pago asociado a una liquidación para conocer si este ya fue procesado, se encuentra pendiente o fue rechazado.

**Why this priority**: Es una funcionalidad crítica porque permite al usuario confirmar si recibirá el dinero o si debe realizar alguna acción adicional.

**Independent Test**: Puede probarse consultando el estado de un pago previamente registrado, verificando que el sistema muestre correctamente su estado.

**Acceptance Scenarios**:

1. **Scenario**: Visualización de pago aprobado
   - **Given** El usuario tiene un pago registrado como aprobado
   - **When** Consulta el estado del pago
   - **Then** El sistema muestra el estado "Pagado"

2. **Scenario**: Visualización de pago pendiente
   - **Given** El pago aún no ha sido procesado
   - **When** El usuario consulta el estado del pago 
   - **Then** El sistema muestra el estado "Pendiente"

---

### User Story 2 - Consultar detalle del pago (Priority: P2)

Como usuario deseo visualizar informacion a detalle del pago, para entender valores, fechas y métodos utilizados.

**Why this priority**: Aporta transparencia al proceso financiero y evita reclamos por desconocimiento del monto

**Independent Test**: Puede probarse seleccionando un pago específico y verificando que el sistema despliegue toda la información relacionada al pago.

**Acceptance Scenarios**:

1. **Scenario**: Visualización del detalle del pago
   - **Given** Existe un pago asociado al usuario
   - **When** Consulta los detalles del pago.
   - **Then** el sistema muestra monto, fecha, Ajustes/penalidades y estado del pago

---

### Edge Cases



- What happens when el pago no existe o fue eliminado?
- How does system handle muestra un mensaje indicando que no se encontró información del pago.
- What happens when el usuario intenta visualizar un pago que no le pertenece?
- How does system handle debe bloquear el acceso al usuario.

## Requirements *(mandatory)*



### Functional Requirements

- **FR-001**: System MUST permitir al usuario consultar el estado actual de un pago
- **FR-002**: System MUST mostrar estados válidos del pago. 
- **FR-003**: Users MUST be able to visualizar el detalle completo de pago
- **FR-004**: System MUST asociar cada pago a un usuario especifico.
- **FR-005**: System MUST restringir la visualización únicamente al propetario del pago.


### Key Entities *(include if feature involves data)*

- **[Pago ]**: Representa la transacción economica realizada (IdPago, monton, método de pago, fecha)
- **[Usuario]**: Persona que recibe o consulta el pago. Se relaciona con uno o varios pagos (idUser, idPago, nombre)
- **[EstadoPago]**: Representa la condición actual del pago (IdEstadoPago, idPago, estado)

## Success Criteria *(mandatory)*



### Measurable Outcomes

- **SC-001**: El sistema refleja cambios en el estado del pago sin inconsistencias en el 100% de los casos.
- **SC-002**: Al menos el 90% de los usuarios logra consultar el estado del pago sin asistencia externa.
