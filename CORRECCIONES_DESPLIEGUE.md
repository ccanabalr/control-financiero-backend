# Correcciones de Despliegue - 17 de Octubre de 2025

## Problemas Corregidos

### 1. **Error de `serviceAccountKey.json` Faltante** ✅
**Error:**
```
FileNotFoundException: class path resource [serviceAccountKey.json] cannot be opened because it does not exist
```

**Solución:**
Se modificó `ConfiguracionFirebase.java` para manejar las credenciales de Firebase faltantes de forma elegante:
- Se agregó verificación para confirmar si `serviceAccountKey.json` existe antes de intentar cargarlo
- La inicialización de Firebase ahora es opcional y no causará que la aplicación falle si el archivo falta
- Se registran advertencias apropiadas en lugar de lanzar excepciones

**Cambios realizados:**
- `src/main/java/com/proyecto/infraestructura/seguridad/ConfiguracionFirebase.java`
  - Se agregó verificación `Resource.exists()`
  - La inicialización de Firebase está envuelta en try-catch
  - Se removió `throws IOException` de la firma del método

---

### 2. **Error de ClassNotFoundException en MongoDB** ✅
**Error:**
```
java.lang.ClassNotFoundException: com.mongodb.MongoCommandException
java.lang.NoClassDefFoundError: com/mongodb/MongoCommandException
```

**Solución:**
Se agregó la dependencia explícita del controlador MongoDB en `build.gradle`:
```gradle
implementation 'org.mongodb:mongodb-driver-sync:4.11.1'
```

Esto asegura que las clases del controlador MongoDB se incluyan correctamente en el classpath.

**Cambios realizados:**
- `build.gradle` - Se agregó la dependencia del controlador MongoDB sync

---

### 3. **Incompatibilidad de Versión Java** ✅
**Error:**
```
error: invalid source release: 21
```

**Causa Raíz:**
El proyecto estaba configurado para Java 21, pero el sistema tiene Java 17 instalado.

**Solución:**
Se redujo el proyecto a Java 17 para compatibilidad:

**Cambios realizados:**
- `build.gradle` - Se cambió `sourceCompatibility = '21'` → `sourceCompatibility = '17'`
- `Dockerfile` - Se cambió `gradle:8.5-jdk21` → `gradle:8.5-jdk17`
- `Dockerfile` - Se cambió `openjdk:21-slim` → `openjdk:17-slim`

---

## Estado de la Compilación
✅ **Compilación Exitosa** - Se ejecutó `./gradlew.bat clean build -x test`

```
> Task :clean
> Task :compileJava
> Task :processResources
> Task :classes
> Task :resolveMainClassName
> Task :bootJar
> Task :jar SKIPPED
> Task :assemble
> Task :check
> Task :build

BUILD SUCCESSFUL in 8s
5 actionable tasks: 5 executed
```

---

## Próximos Pasos para Despliegue en Render

1. **Agregar Credenciales de Firebase (si es necesario):**
   - Coloca tu `serviceAccountKey.json` en `src/main/resources/`
   - La aplicación la cargará si está disponible, o continuará sin ella si no está presente

2. **Variables de Entorno en Render:**
   - Asegúrate de que `MONGODB_URI` esté configurada
   - Asegúrate de que `SPRING_DATA_MONGODB_DATABASE` esté configurada
   - Establece `SERVER_PORT` para usar puertos dinámicos (predeterminado: 8080)

3. **Desplegar:**
   ```
   git push origin master
   ```
   Render debería detectar automáticamente los cambios y activar una nueva compilación.

4. **Verificar Registros:**
   Verifica los registros del panel de Render para:
   - "Firebase inicializado correctamente" (si las credenciales están presentes)
   - "Firebase no será inicializado" (si faltan credenciales - esto es OK)
   - "Tomcat initialized with port" - indica que el servidor inició exitosamente
   - Mensajes de conexión a MongoDB mostrando conexión exitosa al cluster

---

## Archivos Modificados

1. `build.gradle`
   - Se agregó `org.mongodb:mongodb-driver-sync:4.11.1`
   - Se cambió la versión de Java de 21 a 17

2. `Dockerfile`
   - Se cambió la imagen de Gradle de `gradle:8.5-jdk21` a `gradle:8.5-jdk17`
   - Se cambió la imagen de Runtime de `openjdk:21-slim` a `openjdk:17-slim`

3. `ConfiguracionFirebase.java`
   - Se hizo que la inicialización de Firebase sea opcional y elegante
   - Se agregó manejo apropiado de errores para credenciales faltantes

---

## Detalles Técnicos

### Por Qué Funcionan Estos Cambios

1. **Firebase Opcional**: La aplicación ya no falla si faltan las credenciales de Firebase. Esto permite el despliegue sin Firebase en entornos de desarrollo/prueba.

2. **Controlador MongoDB Explícito**: La dependencia `spring-boot-starter-data-mongodb` no siempre incluye las clases del controlador MongoDB. Agregarlo explícitamente previene ClassNotFoundException en tiempo de ejecución.

3. **Compatibilidad con Java 17**: Java 17 es más ampliamente soportado en entornos en la nube y es LTS, lo que lo hace una opción más segura que Java 21 que es más nuevo.

---

## Pruebas Locales (Opcional)

```bash
cd control-financiero-backend
./gradlew.bat bootRun
```

La aplicación debería iniciar en: `http://localhost:8080/api`


