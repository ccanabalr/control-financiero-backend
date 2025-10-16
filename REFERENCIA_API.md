# 📖 REFERENCIA DE API - Endpoints REST

## Base URL

```
http://localhost:8080/api/v1
```

## Autenticación

Todos los endpoints protegidos requieren enviar un JWT de Firebase en el header:

```
Authorization: Bearer <FIREBASE_JWT_TOKEN>
```

## Endpoints Públicos

### Health Check
```
GET /api/health
```

**Respuesta**: `200 OK`
```json
{
  "estado": "OK",
  "servicio": "Control Financiero Backend",
  "version": "1.0.0"
}
```

---

## Endpoints Protegidos - Transacciones

### 1. Crear Transacción
```
POST /api/v1/transacciones
```

**Headers**:
```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

**Body**:
```json
{
  "cuentaId": "cuenta_123",
  "tipo": "GASTO",
  "monto": 50.00,
  "descripcion": "Compra de alimentos",
  "categoriaId": "categoria_456",
  "fechaTransaccion": "2025-10-16T14:30:00",
  "etiquetas": ["comida", "groceries"]
}
```

**Respuesta**: `201 Created`
```json
{
  "id": "transaccion_789",
  "usuarioId": "user_firebase_uid",
  "cuentaId": "cuenta_123",
  "categoriaId": "categoria_456",
  "tipo": "GASTO",
  "monto": 50.00,
  "descripcion": "Compra de alimentos",
  "fechaTransaccion": "2025-10-16T14:30:00",
  "fechaCreacion": "2025-10-16T15:00:00",
  "fechaActualizacion": null,
  "etiquetas": ["comida", "groceries"],
  "referenciaExterna": null
}
```

**Validaciones**:
- `cuentaId`: Requerido, no vacío
- `tipo`: Requerido, debe ser "INGRESO" o "GASTO"
- `monto`: Requerido, debe ser > 0

**Errores**:
- `400 Bad Request`: Validación fallida
- `401 Unauthorized`: Token ausente o inválido
- `500 Internal Server Error`: Error del servidor

---

### 2. Obtener Transacción por ID
```
GET /api/v1/transacciones/{id}
```

**Ejemplo**:
```
GET /api/v1/transacciones/transaccion_789
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Respuesta**: `200 OK`
```json
{
  "id": "transaccion_789",
  "usuarioId": "user_firebase_uid",
  "cuentaId": "cuenta_123",
  ...
}
```

**Errores**:
- `404 Not Found`: Transacción no existe
- `403 Forbidden`: No es propietario de la transacción

---

### 3. Listar Transacciones del Usuario
```
GET /api/v1/transacciones
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Respuesta**: `200 OK`
```json
[
  {
    "id": "transaccion_789",
    "usuarioId": "user_uid",
    "cuentaId": "cuenta_123",
    "tipo": "GASTO",
    "monto": 50.00,
    ...
  },
  {
    "id": "transaccion_790",
    "usuarioId": "user_uid",
    "cuentaId": "cuenta_123",
    "tipo": "INGRESO",
    "monto": 1000.00,
    ...
  }
]
```

---

### 4. Listar Transacciones de una Cuenta
```
GET /api/v1/transacciones/cuenta/{cuentaId}
```

**Ejemplo**:
```
GET /api/v1/transacciones/cuenta/cuenta_123
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Respuesta**: `200 OK`
```json
[
  { /* transacción 1 */ },
  { /* transacción 2 */ }
]
```

---

### 5. Actualizar Transacción
```
PUT /api/v1/transacciones/{id}
```

**Ejemplo**:
```
PUT /api/v1/transacciones/transaccion_789
```

**Headers**:
```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

**Body**:
```json
{
  "cuentaId": "cuenta_123",
  "tipo": "GASTO",
  "monto": 55.00,
  "descripcion": "Compra de alimentos (actualizado)",
  "categoriaId": "categoria_456",
  "etiquetas": ["comida", "groceries", "importante"]
}
```

**Respuesta**: `200 OK`
```json
{
  "id": "transaccion_789",
  "usuarioId": "user_uid",
  ...
  "fechaActualizacion": "2025-10-16T15:30:00"
}
```

**Errores**:
- `404 Not Found`: Transacción no existe
- `403 Forbidden`: No es propietario

---

### 6. Eliminar Transacción
```
DELETE /api/v1/transacciones/{id}
```

**Ejemplo**:
```
DELETE /api/v1/transacciones/transaccion_789
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Respuesta**: `204 No Content`

**Errores**:
- `404 Not Found`: Transacción no existe
- `403 Forbidden`: No es propietario

---

### 7. Obtener Resumen Mensual
```
GET /api/v1/transacciones/resumen-mensual?cuentaId={cuentaId}&anio={anio}&mes={mes}
```

**Ejemplo**:
```
GET /api/v1/transacciones/resumen-mensual?cuentaId=cuenta_123&anio=2025&mes=10
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Parámetros** (Query String):
- `cuentaId` (requerido): ID de la cuenta
- `anio` (opcional): Año (default: actual)
- `mes` (opcional): Mes 1-12 (default: actual)

**Respuesta**: `200 OK`
```json
{
  "id": "resumen_2025_10",
  "usuarioId": "user_uid",
  "cuentaId": "cuenta_123",
  "anio": 2025,
  "mes": 10,
  "saldoInicial": 1000.00,
  "saldoFinal": 1250.00,
  "totalIngresos": 500.00,
  "totalEgresos": 250.00,
  "fechaCreacion": "2025-10-01T10:00:00",
  "fechaActualizacion": "2025-10-16T15:30:00"
}
```

**Errores**:
- `404 Not Found`: Resumen no existe
- `403 Forbidden`: No es propietario

---

### 8. Obtener Resumen Actual
```
GET /api/v1/transacciones/resumen-actual?cuentaId={cuentaId}
```

**Ejemplo**:
```
GET /api/v1/transacciones/resumen-actual?cuentaId=cuenta_123
```

**Headers**:
```
Authorization: Bearer <TOKEN>
```

**Parámetros**:
- `cuentaId` (requerido): ID de la cuenta

**Respuesta**: `200 OK` (mismo formato que resumen mensual)

---

## Códigos de Respuesta

| Código | Significado |
|--------|------------|
| `200` | OK - Solicitud exitosa |
| `201` | Created - Recurso creado |
| `204` | No Content - Eliminado exitosamente |
| `400` | Bad Request - Datos inválidos |
| `401` | Unauthorized - Token ausente o inválido |
| `403` | Forbidden - Sin permiso |
| `404` | Not Found - Recurso no encontrado |
| `500` | Internal Server Error - Error del servidor |

---

## Estructura de Errores

### Errores de Validación (400)
```json
{
  "timestamp": "2025-10-16T15:00:00",
  "estado": 400,
  "mensaje": "Error de validacion",
  "errores": {
    "monto": "El monto debe ser positivo",
    "tipo": "El tipo de transaccion es requerido"
  },
  "ruta": "/api/v1/transacciones"
}
```

### Errores Generales
```json
{
  "mensaje": "Solicitud inválida",
  "detalle": "El monto debe ser mayor a cero",
  "codigoEstado": 400,
  "timestamp": "2025-10-16T15:00:00",
  "ruta": "/api/v1/transacciones"
}
```

---

## Ejemplos con cURL

### Crear Transacción
```bash
curl -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_123",
    "tipo": "GASTO",
    "monto": 50.00,
    "descripcion": "Compra",
    "categoriaId": "cat_456"
  }'
```

### Listar Transacciones
```bash
curl -X GET http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer <TOKEN>"
```

### Obtener Resumen
```bash
curl -X GET "http://localhost:8080/api/v1/transacciones/resumen-actual?cuentaId=cuenta_123" \
  -H "Authorization: Bearer <TOKEN>"
```

### Eliminar Transacción
```bash
curl -X DELETE http://localhost:8080/api/v1/transacciones/transaccion_789 \
  -H "Authorization: Bearer <TOKEN>"
```

---

## Ejemplos con Postman

### 1. Crear Collection
- Name: `Control Financiero API`

### 2. Crear Environment
```json
{
  "base_url": "http://localhost:8080/api",
  "firebase_token": "<TU_TOKEN>",
  "cuenta_id": "cuenta_123"
}
```

### 3. Headers Globales
```
Authorization: Bearer {{firebase_token}}
Content-Type: application/json
```

### 4. Peticiones Recomendadas
- `POST {{base_url}}/v1/transacciones` - Crear
- `GET {{base_url}}/v1/transacciones` - Listar
- `GET {{base_url}}/v1/transacciones/resumen-actual?cuentaId={{cuenta_id}}` - Resumen

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints protegidos requieren token válido
2. **Seguridad**: Las transacciones están filtradas por usuario autenticado
3. **Validaciones**: Los DTOs validan automáticamente los datos
4. **CORS**: Configurado para localhost (desarrollo)
5. **Zona Horaria**: UTC (LocalDateTime)

---

## Próximos Endpoints (Frente 2.4)

- Cuentas (CRUD)
- Categorías (CRUD)
- Usuarios (Profile, Actualizar)
- Reportes (Gastos por categoría, Tendencias)
- Importar/Exportar (CSV)
