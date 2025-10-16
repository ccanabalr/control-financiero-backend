# 🧪 GUÍA DE TESTING - Control Financiero API

## 1. Verificación Previa

### Requisitos
- ✅ Java 21 LTS instalado
- ✅ Maven/Gradle funcional
- ✅ MongoDB Atlas configurado
- ✅ Firebase Admin SDK configurado (serviceAccountKey.json)
- ✅ Spring Boot 3.2.0 LTS

### Pasos Previos
```bash
# 1. Compilar el proyecto
cd d:\personal\control-financiero-backend
gradle clean build

# 2. Verificar que no haya errores
gradle test --info

# 3. Iniciar la aplicación
gradle bootRun
```

---

## 2. Testing Manual con cURL

### 2.1 Health Check (Sin Autenticación)
```bash
curl -v http://localhost:8080/api/health
```

**Respuesta esperada**: 
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "estado": "OK",
  "servicio": "Control Financiero Backend",
  "version": "1.0.0"
}
```

---

### 2.2 Obtener Token Firebase

**Opción A: Usar Usuario de Prueba en Firebase Console**
1. Ir a Firebase Console → Authentication → Users
2. Crear usuario de prueba: test@example.com / password123
3. Obtener el ID Token usando REST API

```bash
# Reemplaza con TU API KEY de Firebase
curl -X POST https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=<YOUR_FIREBASE_API_KEY> \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "returnSecureToken": true
  }'
```

**Respuesta**:
```json
{
  "localId": "firebase_uid_aqui",
  "idToken": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjMwNDM4ZWQ3Nzg2OTUxNDE1YTQzN2M5MzZkYjE2MjE1MzA3YTUzNjMiLCJ0eXAiOiJKV1QifQ...",
  "email": "test@example.com",
  "refreshToken": "...",
  "expiresIn": "3600"
}
```

**Opción B: Usar Firebase Admin SDK (Backend)**
```bash
# En tu consola de administración
firebase auth:export /tmp/users.json

# Copiar un UID válido
```

---

### 2.3 Crear Transacción

```bash
# Guarda el token en una variable
export FIREBASE_TOKEN="<idToken_del_paso_anterior>"

# Crear una transacción de GASTO
curl -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $FIREBASE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_prueba_001",
    "tipo": "GASTO",
    "monto": 50.00,
    "descripcion": "Compra de alimentos en supermercado",
    "categoriaId": "cat_alimentos",
    "etiquetas": ["comida", "groceries"]
  }'
```

**Respuesta esperada** (201 Created):
```json
{
  "id": "5f9d88a0d3b4f5a6c1e2b3c4",
  "usuarioId": "firebase_uid_aqui",
  "cuentaId": "cuenta_prueba_001",
  "tipo": "GASTO",
  "monto": 50.00,
  "descripcion": "Compra de alimentos en supermercado",
  "categoriaId": "cat_alimentos",
  "fechaTransaccion": "2025-10-16T15:30:00",
  "fechaCreacion": "2025-10-16T15:30:00",
  "etiquetas": ["comida", "groceries"]
}
```

---

### 2.4 Listar Transacciones

```bash
curl -X GET http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $FIREBASE_TOKEN"
```

**Respuesta esperada** (200 OK):
```json
[
  {
    "id": "5f9d88a0d3b4f5a6c1e2b3c4",
    "usuarioId": "firebase_uid_aqui",
    "cuentaId": "cuenta_prueba_001",
    "tipo": "GASTO",
    "monto": 50.00,
    ...
  }
]
```

---

### 2.5 Obtener Transacción por ID

```bash
# Reemplaza el ID con el que recibiste en la respuesta anterior
export TRANSACCION_ID="5f9d88a0d3b4f5a6c1e2b3c4"

curl -X GET http://localhost:8080/api/v1/transacciones/$TRANSACCION_ID \
  -H "Authorization: Bearer $FIREBASE_TOKEN"
```

---

### 2.6 Actualizar Transacción

```bash
curl -X PUT http://localhost:8080/api/v1/transacciones/$TRANSACCION_ID \
  -H "Authorization: Bearer $FIREBASE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_prueba_001",
    "tipo": "GASTO",
    "monto": 65.00,
    "descripcion": "Compra actualizada - incluye bebidas",
    "categoriaId": "cat_alimentos",
    "etiquetas": ["comida", "groceries", "importante"]
  }'
```

---

### 2.7 Obtener Resumen Mensual

```bash
# Resumen del mes actual
curl -X GET "http://localhost:8080/api/v1/transacciones/resumen-actual?cuentaId=cuenta_prueba_001" \
  -H "Authorization: Bearer $FIREBASE_TOKEN"
```

**Respuesta esperada** (200 OK):
```json
{
  "id": "resumen_2025_10",
  "usuarioId": "firebase_uid_aqui",
  "cuentaId": "cuenta_prueba_001",
  "anio": 2025,
  "mes": 10,
  "saldoInicial": 1000.00,
  "saldoFinal": 935.00,
  "totalIngresos": 0.00,
  "totalEgresos": 65.00,
  "fechaCreacion": "2025-10-01T10:00:00",
  "fechaActualizacion": "2025-10-16T15:30:00"
}
```

---

### 2.8 Eliminar Transacción

```bash
curl -X DELETE http://localhost:8080/api/v1/transacciones/$TRANSACCION_ID \
  -H "Authorization: Bearer $FIREBASE_TOKEN"
```

**Respuesta esperada** (204 No Content) - Sin body

---

## 3. Testing con Postman

### 3.1 Crear Collection

**Pasos**:
1. Abrir Postman
2. Click en "+" o "New" → Collection
3. Nombre: `Control Financiero API`
4. Guardar

### 3.2 Configurar Environment

1. Click en "Environments" (rueda dentada)
2. New → Nombre: `Development`
3. Agregar variables:

```json
{
  "base_url": "http://localhost:8080/api",
  "firebase_token": "{{PASTE_YOUR_TOKEN_HERE}}",
  "cuenta_id": "cuenta_prueba_001",
  "transaccion_id": ""
}
```

### 3.3 Crear Requests

#### Request 1: Health Check
```
GET {{base_url}}/health
```

#### Request 2: Crear Transacción
```
POST {{base_url}}/v1/transacciones

Body (raw JSON):
{
  "cuentaId": "{{cuenta_id}}",
  "tipo": "GASTO",
  "monto": 50.00,
  "descripcion": "Test transacción",
  "categoriaId": "cat_test",
  "etiquetas": ["test"]
}

Header:
Authorization: Bearer {{firebase_token}}
```

**Script de Post-Request** (para guardar el ID):
```javascript
if (pm.response.code === 201) {
  const responseBody = pm.response.json();
  pm.environment.set("transaccion_id", responseBody.id);
  console.log("Transacción creada: " + responseBody.id);
}
```

#### Request 3: Listar Transacciones
```
GET {{base_url}}/v1/transacciones

Header:
Authorization: Bearer {{firebase_token}}
```

#### Request 4: Obtener Transacción
```
GET {{base_url}}/v1/transacciones/{{transaccion_id}}

Header:
Authorization: Bearer {{firebase_token}}
```

#### Request 5: Actualizar Transacción
```
PUT {{base_url}}/v1/transacciones/{{transaccion_id}}

Body (raw JSON):
{
  "cuentaId": "{{cuenta_id}}",
  "tipo": "INGRESO",
  "monto": 100.00,
  "descripcion": "Transacción actualizada",
  "categoriaId": "cat_salario"
}

Header:
Authorization: Bearer {{firebase_token}}
```

#### Request 6: Resumen Actual
```
GET {{base_url}}/v1/transacciones/resumen-actual?cuentaId={{cuenta_id}}

Header:
Authorization: Bearer {{firebase_token}}
```

#### Request 7: Eliminar Transacción
```
DELETE {{base_url}}/v1/transacciones/{{transaccion_id}}

Header:
Authorization: Bearer {{firebase_token}}
```

---

## 4. Testing de Validaciones

### Test: Monto Negativo
```bash
curl -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $FIREBASE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_prueba_001",
    "tipo": "GASTO",
    "monto": -50.00,
    "descripcion": "Monto negativo"
  }'
```

**Respuesta esperada** (400 Bad Request):
```json
{
  "timestamp": "2025-10-16T15:35:00",
  "estado": 400,
  "mensaje": "Error de validacion",
  "errores": {
    "monto": "El monto debe ser positivo"
  },
  "ruta": "/api/v1/transacciones"
}
```

---

### Test: Tipo Inválido
```bash
curl -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $FIREBASE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_prueba_001",
    "tipo": "INVALIDO",
    "monto": 50.00,
    "descripcion": "Tipo inválido"
  }'
```

**Respuesta esperada** (400 Bad Request)

---

### Test: Sin Autorización
```bash
curl -X GET http://localhost:8080/api/v1/transacciones
```

**Respuesta esperada** (401 Unauthorized)

---

### Test: Token Expirado/Inválido
```bash
curl -X GET http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer token_invalido_aqui"
```

**Respuesta esperada** (401 Unauthorized)

---

## 5. Testing de Flujo Completo

### Escenario: Ciclo de Vida de Transacción

```bash
#!/bin/bash

# 1. Obtener token
TOKEN="$FIREBASE_TOKEN"

echo "1. Creando transacción..."
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_test",
    "tipo": "GASTO",
    "monto": 25.50,
    "descripcion": "Gastos de transporte"
  }')

ID=$(echo $CREATE_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "✓ Transacción creada: $ID"

# 2. Obtener la transacción
echo "2. Obteniendo transacción..."
curl -s -X GET http://localhost:8080/api/v1/transacciones/$ID \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo "✓ Transacción recuperada"

# 3. Actualizar
echo "3. Actualizando transacción..."
curl -s -X PUT http://localhost:8080/api/v1/transacciones/$ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "cuenta_test",
    "tipo": "GASTO",
    "monto": 30.00,
    "descripcion": "Gastos de transporte (actualizado)"
  }' | jq '.'
echo "✓ Transacción actualizada"

# 4. Listar todas
echo "4. Listando todas las transacciones..."
curl -s -X GET http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo "✓ Listado completo"

# 5. Obtener resumen
echo "5. Obteniendo resumen..."
curl -s -X GET "http://localhost:8080/api/v1/transacciones/resumen-actual?cuentaId=cuenta_test" \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo "✓ Resumen obtenido"

# 6. Eliminar
echo "6. Eliminando transacción..."
curl -s -X DELETE http://localhost:8080/api/v1/transacciones/$ID \
  -H "Authorization: Bearer $TOKEN"
echo "✓ Transacción eliminada"
```

Guardar como `test_completo.sh` y ejecutar:
```bash
chmod +x test_completo.sh
./test_completo.sh
```

---

## 6. Testing de Performance

### Crear 100 Transacciones
```bash
#!/bin/bash

TOKEN="$FIREBASE_TOKEN"

for i in {1..100}; do
  curl -s -X POST http://localhost:8080/api/v1/transacciones \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"cuentaId\": \"cuenta_test\",
      \"tipo\": \"GASTO\",
      \"monto\": $((RANDOM % 100)).50,
      \"descripcion\": \"Transacción de prueba $i\"
    }" > /dev/null
  
  echo "Creada transacción $i/100"
done

echo "✓ 100 transacciones creadas"
```

---

## 7. Testing de Seguridad

### Test 1: Acceder a Recurso de Otro Usuario
```bash
# Usuario 1 obtiene su token y ve transacciones de usuario 2
# Debería fallar con 403 Forbidden
```

### Test 2: Token Expirado
```bash
# Generar token que expire en 1 segundo
# Esperar y intentar usar → 401 Unauthorized
```

### Test 3: SQL Injection (MongoDB)
```bash
curl -X POST http://localhost:8080/api/v1/transacciones \
  -H "Authorization: Bearer $FIREBASE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": "{ $ne: null }",
    "tipo": "GASTO",
    "monto": 50
  }'
```
**Esperado**: Rechazado o tratado como string literal

---

## 8. Comandos útiles

### Ver logs en tiempo real
```bash
gradle bootRun --info
```

### Detener aplicación
```
CTRL+C
```

### Limpiar y recompilar
```bash
gradle clean build --refresh-dependencies
```

### Ejecutar solo tests
```bash
gradle test
```

### Ver dependencias
```bash
gradle dependencies
```

---

## 9. Checklist de Testing

- [ ] Health check responde
- [ ] Crear transacción exitosamente
- [ ] Listar transacciones
- [ ] Obtener transacción individual
- [ ] Actualizar transacción
- [ ] Eliminar transacción
- [ ] Obtener resumen mensual
- [ ] Obtener resumen actual
- [ ] Validación de monto negativo
- [ ] Validación de tipo inválido
- [ ] Acceso sin token (401)
- [ ] Token inválido (401)
- [ ] Transacción de otro usuario (403)
- [ ] Crear 100 transacciones sin errores
- [ ] Resumen actualiza correctamente

---

## 10. Troubleshooting

### Error: "Connection refused"
```
Solución: Verificar que la aplicación está running
gradle bootRun
```

### Error: "401 Unauthorized"
```
Solución: Verificar token válido y no expirado
- Generar nuevo token en Firebase Console
- Asegurar Header correcto: Authorization: Bearer <TOKEN>
```

### Error: "403 Forbidden"
```
Solución: Verificar que el usuario es propietario del recurso
- La transacción debe pertenecer al usuarioId del token
```

### Error: "400 Bad Request"
```
Solución: Verificar validaciones en DTO
- Revisar mensaje de error retornado
- Validar monto > 0
- Validar tipo en [INGRESO, GASTO]
- Validar cuentaId requerido
```

### Error: "500 Internal Server Error"
```
Solución: Revisar logs de la aplicación
- Buscar exceptions en gradle bootRun output
- Verificar conexión a MongoDB Atlas
- Verificar configuración de Firebase
```

---

## 11. Próximos Tests

- Unit tests para ServicioTransaccionImpl
- Integration tests para ControladorTransaccion
- Load testing con JMeter
- Contract testing con Pact
- API documentation con Swagger
