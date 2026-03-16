# Feature Specification: Registrar pagos exitosos

**Created**: 23/02/2026  

## User Scenarios & Testing *(mandatory)*

Efectuado satisfactoriamente el pago realizado por la entidad correspondiente a partir de la liquidacion,
el funcionario denominado "gestor de cartera" debe anexar manualmente el comorobante de pago correspondiente y todos la información relevante de dicha transacción.

### User Story 1 - Registrar un pago exitoso (Priority: P1)

Como gestor de cartera, quiero que el sistema me permita ingresar, guardar y consultar diferentes pagos exitosos con su respectivo comprobante.


**Why this priority**: Es indispensable para la trazabilidad financiera y cerrar el ciclo de la liquidación.


**Independent Test**: Seleccionar una liquidación en estado, ingresar los datos de la transacción (referencia, fecha, monto, método de pago), adjuntar un archivo válido (PDF/JPG) como comprobante y verificar que el sistema guarde el registro y actualice el estado de la liquidación.


**Acceptance Scenarios**:

1. **Scenario**: Cálculo exitoso de liquidación.
   - **Given** Un modelo de contratación creado, el informe del estado del paquete e informe de cierre de ruta.
   - **When** El usuario solicita el cálculo de liquidación.
   - **Then**  El sistema calcula el valor final de la liquidación
   - **And** registra la liquidación asociada a la ruta.

    
---

### User Story 2 - Recalcular liquidación (Priority: P2)

Como gestor de contratación, quiero recalcular la liquidación si se registran nuevos ajustes para mantener la información actualizada.

**Why this priority**: Permite la información actualizada cuando se registra nuevos pagos o ajustes posteriores.

**Independent Test**:Se puede probar agregar un nuevo ajuste de pago y ejecutando la opción de recalculo.

**Acceptance Scenarios**:

1. **Scenario**: Recalcular por nueva liquidación.
   - **Given** Una liquidación ya calculada.
   - **When** El usuario solicita recalcular.
   - **Then** El sistema actualiza valor final de la liquidación 
   - **And** Registra auditoria del recalculo.

---



### Edge Cases

- What happens when existe el modelo de contratación en una ruta no existente?
- How does system handle Debe reportar un fallo tecnico en la base de datos 
- What happens when si la fecha de finalización es anterior a la fecha de inicio?
- How does system handle El sistema debe impedir el registro y mostrar error de validación.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST calcular automáticamente la liquidación cuando el contrato se encuentra finalizado y el usuario confirma la acción de cálculo.
- **FR-002**: System MUST aplicar penalizaciones y ajustes configurados.  
- **FR-003**: System MUST Registrar auditoría del cálculo y recálculo.
- **FR-004**: System MUST evitar la generación de liquidaciones duplicadas para un mismo contrato.

### Key Entities 

- **[Contrato]**:Representa un contrato entre la empresa y el trasportista (IdContrato, Tipo de contratación).
- **[Ruta]**:Representa una ruta que debe ser liquidada (IdRuta, FechaInicio, FechaCierre).
- **[liquidación]**: Representa el valor de la liquidación (idLiquidacion, idContrato, idRuta, estadoLiquidacion, valorFinal, fechaCalculo, idPenalidad, estadoPenalidad)
- **[Estado del paquete]**: Representa los estados finales de los paquetes y los motivos de su estado final (idPaquete, novedades, estadoFinal)
- **[Ajustes/penalización]**: Representa los ajustes del pago final de la liquidación, esta existe para relacionar los diferentes estados que puede estar el paquete (idPenalización, tipo, monto, razón) 

## Success Criteria *(mandatory)*



### Measurable Outcomes

- **SC-001**: El 100% de los contratos pueden ser liquidados correctamente.
- **SC-002**: No existen discrepancias entre valor calculado y valor almacenado y mostrado.
- **SC-003**: El sistema evita el 100% de liquidaciones duplicadas.

