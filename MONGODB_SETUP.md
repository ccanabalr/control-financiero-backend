# Configuración de MongoDB Atlas

## Paso 1: Crear cuenta en MongoDB Atlas

1. Ir a https://www.mongodb.com/cloud/atlas
2. Crear una cuenta gratuita (no requiere tarjeta de crédito)
3. Crear una Organización y Proyecto

## Paso 2: Crear un Cluster M0 (Gratuito)

1. Seleccionar "Build a Cluster"
2. Elegir "Shared Clusters" (gratis)
3. Seleccionar proveedor (AWS, GCP, Azure)
4. Seleccionar región (elegir la más cercana)
5. Nombre del cluster: `cluster0`
6. Crear el cluster

## Paso 3: Crear un Usuario de Base de Datos

1. En "Database Access", crear un nuevo usuario
2. Usar autenticación por "Password"
3. Guardar el usuario y contraseña de forma segura

## Paso 4: Configurar IP Whitelist

1. En "Network Access", agregar tu IP
2. O agregar `0.0.0.0/0` para permitir todas (solo desarrollo)

## Paso 5: Obtener Connection String

1. En la página del cluster, hacer clic en "Connect"
2. Seleccionar "Connect your application"
3. Elegir "Java"
4. Copiar la cadena de conexión

## Paso 6: Configurar en application.yml

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<usuario>:<contrasena>@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority
      auto-index-creation: true
```

Reemplazar:
- `<usuario>`: Usuario creado en el Paso 3
- `<contrasena>`: Contraseña creada en el Paso 3

## Colecciones a Crear

Las siguientes colecciones se crearán automáticamente:

- `usuarios` - Usuarios del sistema
- `cuentas` - Cuentas de los usuarios
- `categorias` - Categorías de transacciones
- `transacciones` - Registro de transacciones
- `resumenes_mensuales` - Resúmenes por período

## Índices Recomendados

Para mejorar el rendimiento, crear estos índices en MongoDB:

```javascript
// En usuarios
db.usuarios.createIndex({ "firebase_uid": 1 }, { unique: true })
db.usuarios.createIndex({ "correo_electronico": 1 }, { unique: true })

// En cuentas
db.cuentas.createIndex({ "usuario_id": 1 })

// En categorias
db.categorias.createIndex({ "usuario_id": 1 })

// En transacciones
db.transacciones.createIndex({ "usuario_id": 1 })
db.transacciones.createIndex({ "cuenta_id": 1 })
db.transacciones.createIndex({ "categoria_id": 1 })
db.transacciones.createIndex({ "fecha_transaccion": -1 })

// En resumenes_mensuales
db.resumenes_mensuales.createIndex({ "usuario_id": 1 })
db.resumenes_mensuales.createIndex({ "cuenta_id": 1 })
db.resumenes_mensuales.createIndex({ "cuenta_id": 1, "anio": 1, "mes": 1 }, { unique: true })
```

## Monitoreo

- Usar MongoDB Compass (descarga gratuita) para gestionar datos localmente
- En Atlas, usar la pestaña "Metrics" para monitorear el uso
- El tier M0 tiene límites: 512MB almacenamiento, sin backups automáticos
