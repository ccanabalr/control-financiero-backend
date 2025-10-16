# 🧪 GUÍA DE TESTING LOCAL

## Prerrequisitos

1. **Java 21 instalado**
   ```bash
   java -version
   ```

2. **Gradle instalado**
   ```bash
   gradle -v
   ```

3. **MongoDB Atlas configurado**
   - Leer `MONGODB_SETUP.md`
   - Tener URI lista

4. **Firebase configurado**
   - Leer `FIREBASE_SETUP.md`
   - Tener `serviceAccountKey.json` en `src/main/resources/`

## Paso 1: Configurar Variables de Entorno

### Windows PowerShell
```powershell
$env:MONGODB_URI="mongodb+srv://<usuario>:<pass>@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority"
```

### Linux/Mac
```bash
export MONGODB_URI="mongodb+srv://<usuario>:<pass>@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority"
```

## Paso 2: Construir el Proyecto

```bash
cd d:\personal\control-financiero-backend

# Limpiar y construir
gradle clean build

# O sin tests
gradle clean build -x test
```

## Paso 3: Ejecutar la Aplicación

```bash
gradle bootRun
```

**Resultado esperado**:
```
2025-10-16 12:00:00.000  INFO  com.proyecto.ControlFinancieroAplicacion     : 
  Started ControlFinancieroAplicacion in 5.234 seconds (process running for 5.478)
```

## Paso 4: Verificar Health Endpoint

### Con curl
```bash
curl http://localhost:8080/api/health
```

**Respuesta esperada**:
```json
{
  "estado": "OK",
  "servicio": "Control Financiero Backend",
  "version": "1.0.0"
}
```

### Con Postman
1. Crear nueva request
2. GET `http://localhost:8080/api/health`
3. Enviar

## Paso 5: Intentar Acceso a Endpoint Protegido

### SIN token (debe fallar)
```bash
curl -i http://localhost:8080/api/transacciones
```

**Respuesta esperada**: `401 Unauthorized`

### CON token de Firebase (próximo)

Una vez que el frontend esté listo, incluir el JWT:

```bash
curl -H "Authorization: Bearer <FIREBASE_JWT>" \
     http://localhost:8080/api/transacciones
```

## Paso 6: Testing Manual con Postman

### 1. Crear Collection

```
Control Financiero API
├── Health
│   └── GET /health
├── Transacciones (próximas)
│   ├── POST / (crear)
│   ├── GET / (listar)
│   ├── GET /{id}
│   └── DELETE /{id}
└── Cuentas (próximas)
    ├── POST / (crear)
    ├── GET / (listar)
    └── PUT /{id} (actualizar)
```

### 2. Configurar Environment

```json
{
  "base_url": "http://localhost:8080/api",
  "firebase_token": "<token_aquí>",
  "usuario_id": "<uid_aquí>"
}
```

### 3. Agregar Headers

```
Content-Type: application/json
Authorization: Bearer {{firebase_token}}
```

## Paso 7: Monitorear Logs

### Aumentar nivel de logs
Editar `application.yml`:

```yaml
logging:
  level:
    root: DEBUG
    com.proyecto: DEBUG
    org.springframework.security: DEBUG
```

### Interpretar logs
```
DEBUG com.proyecto.infraestructura.seguridad.FiltroAutenticacionFirebase
  → "Usuario autenticado: <uid>"

DEBUG com.proyecto.infraestructura.persistencia.adaptadores.AdaptadorPersistenciaTransaccion
  → "Obteniendo transacciones del usuario: <uid>"
```

## Paso 8: Testing de Integración con MongoDB

### 1. Conectar con MongoDB Compass

1. Descargar MongoDB Compass
2. Conectar con URI de Atlas
3. Ver colecciones creadas

### 2. Verificar Documentos

```javascript
// En MongoDB Compass, en la colección "usuarios"
db.usuarios.find()

// Resultado: [] (vacío inicialmente)
```

## Paso 9: Testing de Seguridad

### Escenario 1: Sin token
```bash
curl -i http://localhost:8080/api/transacciones
```
→ `401 Unauthorized` ✅

### Escenario 2: Token inválido
```bash
curl -i -H "Authorization: Bearer token_invalido" \
     http://localhost:8080/api/transacciones
```
→ `401 Unauthorized` ✅

### Escenario 3: Token expirado
→ `401 Unauthorized` ✅

### Escenario 4: Token válido
→ `200 OK` (cuando se implemente el endpoint) ✅

## Paso 10: Verificar Conectividad

### MongoDB
```bash
# En Gradle o Java
# Si la conexión falla, verás:
# "org.springframework.data.mongodb.core.MongoException"
```

### Firebase
```bash
# En logs, si hay error:
# "Could not initialize Firebase"
# Verificar: serviceAccountKey.json existe y es válido
```

## Troubleshooting

### ❌ "Could not initialize Firebase"

**Causa**: serviceAccountKey.json falta o es inválido

**Solución**:
1. Descargar de Firebase Console
2. Guardar en `src/main/resources/`
3. Verificar que no esté en `.gitignore`

```bash
# Verificar que existe
ls src/main/resources/serviceAccountKey.json
```

### ❌ "Failed to connect to MongoDB"

**Causa**: URI incorrecta o red sin acceso

**Solución**:
1. Verificar URI en `application.yml`
2. Verificar whitelist de IP en MongoDB Atlas
3. Usar `0.0.0.0/0` para desarrollo local

### ❌ "Port 8080 already in use"

**Causa**: Otra aplicación usando puerto 8080

**Solución**:
```bash
# Cambiar puerto en application.yml
server:
  port: 8081

# O matar proceso
# Windows: netstat -ano | findstr :8080
# Linux: lsof -i :8080
```

### ❌ "Java version mismatch"

**Causa**: Java < 21

**Solución**:
```bash
# Verificar versión
java -version

# Descargar Java 21 LTS
# https://www.oracle.com/java/technologies/downloads/#java21
```

## Paso 11: Testing de Performance

```bash
# Medir tiempo de respuesta
time curl http://localhost:8080/api/health

# Resultado esperado: < 100ms
```

## Paso 12: Testing de Estructura

### Verificar clases de dominio
```java
// En cualquier IDE:
// Click derecho en Usuario.java → Go to Type Hierarchy
// Debe mostrar: Usuario → (es mapeado como) Document MongoDB
```

### Verificar inyección de dependencias
```bash
# En logs al startup:
# "Autowiring by type from bean name 'adapterPersistenciaTransaccion'"
```

## Checklist Pre-Deploy

- ✅ `gradle build` sin errores
- ✅ `gradle bootRun` inicia correctamente
- ✅ GET /health retorna 200
- ✅ GET /transacciones sin token retorna 401
- ✅ Logs sin errores WARNING
- ✅ MongoDB conectada
- ✅ Firebase inicializado
- ✅ application.yml actualizado

## Acciones Post-Testing

1. **Si todo funciona**: ✅ Proceder a Frente 2.3
2. **Si hay errores**: Consultar logs y TROUBLESHOOTING
3. **Si hay dudas**: Revisar ARQUITECTURA.md

## Comandos Útiles

```bash
# Construir sin ejecutar
gradle build -x test

# Ejecutar con output de logs
gradle bootRun --info

# Limpiar artefactos
gradle clean

# Ver dependencias
gradle dependencies

# Ver tareas disponibles
gradle tasks

# Ejecutar solo tests (cuando los haya)
gradle test
```

## URLs de Referencia

- 📖 Spring Boot: https://spring.io/projects/spring-boot
- 📖 MongoDB: https://docs.mongodb.com/
- 📖 Firebase: https://firebase.google.com/docs/
- 📖 Gradle: https://gradle.org/guides/

---

**Fecha de esta guía**: 2025-10-16
**Versión**: 1.0.0
**Estado**: Completa y verificada
