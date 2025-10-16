# Configuración de Firebase Authentication

## Paso 1: Crear Proyecto en Firebase

1. Ir a https://console.firebase.google.com/
2. Hacer clic en "Crear Proyecto"
3. Nombre del proyecto: `control-financiero`
4. Desmarcar "Google Analytics" (no necesario para nivel gratuito)
5. Crear proyecto

## Paso 2: Habilitar Firebase Authentication

1. En el menú lateral, ir a "Authentication"
2. Hacer clic en "Get Started"
3. En "Sign-in method", habilitar:
   - Email/Password
   - (Opcional) Google Sign-In

## Paso 3: Crear Clave de Servicio (Service Account)

1. Ir a "Project Settings" (ícono de engranaje)
2. Seleccionar pestaña "Service Accounts"
3. Hacer clic en "Generate New Private Key"
4. Se descargará un archivo JSON (guardar de forma segura)

## Paso 4: Guardar serviceAccountKey.json

1. Copiar el archivo descargado a: `src/main/resources/serviceAccountKey.json`
2. **IMPORTANTE**: Agregar este archivo a `.gitignore` (ya está en el proyecto)
3. Nunca compartir esta clave

## Paso 5: Configuración en Spring Boot

La clase `ConfiguracionFirebase.java` se encarga de inicializar Firebase automáticamente usando el archivo `serviceAccountKey.json`.

```java
@Configuration
public class ConfiguracionFirebase {
    @PostConstruct
    public void inicializarFirebase() throws IOException {
        // Inicializa automáticamente
    }
}
```

## Flujo de Autenticación

1. **Frontend (Angular)**:
   - Usuario se registra/login en Firebase
   - Recibe un JWT token de Firebase
   - Envía todas las peticiones con `Authorization: Bearer <TOKEN>`

2. **Backend (Java)**:
   - `FiltroAutenticacionFirebase` intercepta las peticiones
   - Valida el token con `FirebaseAuth.getInstance().verifyIdToken(token)`
   - Extrae el UID de Firebase
   - Establece la autenticación en `SecurityContextHolder`

## Obtener el UID del Usuario Autenticado en Controladores

```java
@RestController
public class MiControlador {
    
    @GetMapping("/mi-perfil")
    public ResponseEntity<?> obtenerPerfil(@AuthenticationPrincipal String uid) {
        // uid contiene el Firebase UID del usuario autenticado
        return ResponseEntity.ok("UID: " + uid);
    }
}
```

## Variables de Entorno (Opcional)

Para mayor seguridad, se pueden usar variables de entorno:

```bash
export FIREBASE_PROJECT_ID="tu-proyecto-id"
export FIREBASE_PRIVATE_KEY_ID="tu-id"
export FIREBASE_PRIVATE_KEY="tu-clave-privada"
export FIREBASE_CLIENT_EMAIL="tu-email"
```

Luego leer desde `application.yml`:

```yaml
firebase:
  project-id: ${FIREBASE_PROJECT_ID}
  private-key-id: ${FIREBASE_PRIVATE_KEY_ID}
  private-key: ${FIREBASE_PRIVATE_KEY}
  client-email: ${FIREBASE_CLIENT_EMAIL}
```

## Seguridad de Producción

- Usar secretos de entorno en Render
- No commitear serviceAccountKey.json
- Rotar las claves regularmente
- Monitorear uso en Firebase Console

## Limitaciones del Tier Gratuito

- Máximo 50 eventos de autenticación por día
- Máximo 50 usuarios registrados
- Sin soporte prioritario

**Nota**: Para producción, usar Firebase Plan de Pago (verificación con tarjeta, pero sin compromiso de gasto mínimo).
