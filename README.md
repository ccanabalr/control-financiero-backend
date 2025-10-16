# Control Financiero - Backend (Java/Spring Boot)

## Descripción
Backend de una aplicación de control financiero personal construida con Spring Boot, MongoDB y Firebase Authentication.

## Requisitos Previos
- Java 21 LTS
- Gradle 8.0+
- MongoDB Atlas (M0 Free Tier)
- Firebase Project configurado

## Estructura del Proyecto

```
src/main/java/com/proyecto/
├── dominio/
│   ├── modelo/              # POJOs - Entidades puras
│   │   ├── Usuario.java
│   │   ├── Cuenta.java
│   │   ├── Categoria.java
│   │   ├── Transaccion.java
│   │   └── ResumenMensual.java
│   └── puertos/             # Interfaces (Arquitectura Hexagonal)
│       ├── entrada/         # Casos de uso
│       │   └── ServicioGestionTransaccion.java
│       └── salida/          # Repositorios
│           ├── RepositorioTransaccion.java
│           └── RepositorioResumenMensual.java
├── aplicacion/
│   └── servicios/           # Implementaciones de casos de uso
├── infraestructura/
│   ├── seguridad/           # Firebase Authentication
│   │   ├── ConfiguracionSeguridad.java
│   │   └── FiltroAutenticacionFirebase.java
│   └── persistencia/
│       ├── mongo/           # Interfaces MongoRepository
│       │   ├── RepositorioMongoTransaccion.java
│       │   └── RepositorioMongoResumenMensual.java
│       └── adaptadores/     # Implementaciones de puertos
│           ├── AdaptadorPersistenciaTransaccion.java
│           ├── AdaptadorPersistenciaResumenMensual.java
│           └── UtilUpsertAtomica.java
```

## Configuración

### 1. Variables de Entorno

Crear un archivo `.env` en la raiz del proyecto:

```bash
# Copiar template
cp .env.example .env

# Editar con valores reales
code .env  # o tu editor favorito
```

**Variables necesarias**:
```
MONGODB_URI=mongodb+srv://<usuario>:<contrasena>@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority
FIREBASE_PROJECT_ID=tu-proyecto
FIREBASE_PRIVATE_KEY_ID=tu-id
FIREBASE_PRIVATE_KEY=tu-clave-privada
FIREBASE_CLIENT_EMAIL=tu-email
FIREBASE_CLIENT_ID=tu-id-cliente
```

📖 Ver **[CONFIGURACION_ENV.md](CONFIGURACION_ENV.md)** para guía detallada

### 2. Archivo application.yml

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}
```

## Construcción y Ejecución

### Construir el proyecto
```bash
gradle build
```

### Ejecutar la aplicación
```bash
gradle bootRun
```

La aplicación estará disponible en `http://localhost:8080/api`

## Características Implementadas

### Seguridad (Firebase)
- ✅ Filtro de autenticación personalizado
- ✅ Validación de JWT de Firebase
- ✅ Configuración STATELESS
- ✅ Protección de endpoints

### Persistencia (MongoDB)
- ✅ Adaptadores para Transacciones
- ✅ Adaptadores para Resumenes Mensuales
- ✅ Utilidad para operaciones UPSERT atomicas

### Arquitectura
- ✅ Patrón Hexagonal (Puertos y Adaptadores)
- ✅ Separación clara de capas
- ✅ Inyección de dependencias

## Próximos Pasos
- [ ] Implementar servicios de aplicación
- [ ] Crear controladores REST
- [ ] Agregar validaciones
- [ ] Desplegar en Render

## 🚀 Deploy en Render

Ver **[GUIA_RENDER_DEPLOY.md](GUIA_RENDER_DEPLOY.md)** para instrucciones completas de deployment.

**Resumen rápido**:
```bash
# 1. Commit y push
git push origin main

# 2. En Render: conectar repo y configurar env vars
# 3. Deploy automático iniciará

# URL: https://control-financiero-backend.onrender.com/api/v1
```

## Notas Importantes
- Todo el código está en español, sin tildes ni ñ
- Se usa snake_case en MongoDB con anotaciones @Field
- Arquitectura completamente desacoplada
