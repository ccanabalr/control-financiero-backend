# 🔐 GUÍA DE CONFIGURACIÓN - Variables de Entorno

**Fecha**: 16 Octubre 2025  
**Estado**: ✅ Completo

---

## 📋 Archivos Creados

| Archivo | Propósito | Visible en Git |
|---------|-----------|----------------|
| `.env` | Variables reales (NO subir) | ❌ .gitignore |
| `.env.example` | Template para desarrolladores | ✅ Sí |

---

## 🔑 VARIABLES DE ENTORNO

### MongoDB URI

```
MONGODB_URI=mongodb+srv://<usuario>:<contrasena>@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority
```

**Cómo obtenerlo**:
1. Ir a [MongoDB Atlas](https://cloud.mongodb.com)
2. Seleccionar cluster
3. Click en "Connect"
4. Copiar "Connection String"
5. Reemplazar `<username>` y `<password>` con tus credenciales

**Formato esperado**:
```
mongodb+srv://usuario_mongo:password_mongo@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority
```

---

### Firebase Credentials

Obtén estas variables del archivo `serviceAccountKey.json`:

| Variable | De serviceAccountKey.json | Ejemplo |
|----------|--------------------------|---------|
| `FIREBASE_PROJECT_ID` | `project_id` | `mi-proyecto-12345` |
| `FIREBASE_PRIVATE_KEY_ID` | `private_key_id` | `abc123def456...` |
| `FIREBASE_PRIVATE_KEY` | `private_key` | `-----BEGIN PRIVATE KEY-----\n...` |
| `FIREBASE_CLIENT_EMAIL` | `client_email` | `firebase-adminsdk-abc@mi-proyecto.iam.gserviceaccount.com` |
| `FIREBASE_CLIENT_ID` | `client_id` | `123456789012345` |

**Cómo obtenerlo**:
1. Ir a [Firebase Console](https://console.firebase.google.com)
2. Seleccionar proyecto
3. Ir a Configuración (⚙️) → Cuentas de servicio
4. Click en "Generar clave privada"
5. Se descargará `serviceAccountKey.json`
6. Abrir archivo y copiar valores

---

## 📝 Pasos de Configuración

### 1️⃣ Copiar Template

```bash
# En Linux/Mac
cp .env.example .env

# En Windows PowerShell
Copy-Item .env.example .env
```

### 2️⃣ Editar `.env`

Abrir `.env` y reemplazar placeholders con valores reales:

```bash
# Opción 1: VS Code
code .env

# Opción 2: Editor de texto
nano .env          # Linux/Mac
notepad .env       # Windows
```

### 3️⃣ Verificar Archivo

```bash
# Ver contenido (sin exponerlo en git)
cat .env

# O en PowerShell
Get-Content .env
```

### 4️⃣ Verificar Configuración

```bash
# El archivo está en .gitignore
git status | grep .env

# Debería mostrar: No changes (no aparece .env)
```

---

## 🔒 SEGURIDAD

### ✅ CORRECTO

```bash
.env                    # Local (NO versionado)
.env.example           # GitHub (Sin credenciales)
.env.local             # CI/CD (NO versionado)
.env.production        # Producción (NO versionado)
```

### ❌ INCORRECTO

```bash
# NO hacer esto:
git add .env           # ❌ Expone credenciales
git push               # ❌ A todos los desarrolladores
```

---

## 🚀 Uso en la Aplicación

### En Java/Spring Boot

El archivo `.env` es leído automáticamente por la librería `dotenv-java`:

```java
// application.yml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}

# Variables de Firebase
firebase:
  project-id: ${FIREBASE_PROJECT_ID}
  private-key-id: ${FIREBASE_PRIVATE_KEY_ID}
  private-key: ${FIREBASE_PRIVATE_KEY}
  client-email: ${FIREBASE_CLIENT_EMAIL}
  client-id: ${FIREBASE_CLIENT_ID}
```

### En application.yml

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://usuario:password@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority

firebase:
  project-id: mi-proyecto-12345
  private-key-id: abc123def456
  # ... más variables
```

---

## 📚 Variables por Entorno

### Local Development (`.env`)
```
MONGODB_URI=mongodb+srv://dev_user:dev_pass@cluster0.mongodb.net/control-financiero-dev
FIREBASE_PROJECT_ID=control-financiero-dev
# ... credenciales de desarrollo
```

### Testing (`.env.test`)
```
MONGODB_URI=mongodb+srv://test_user:test_pass@cluster0.mongodb.net/control-financiero-test
FIREBASE_PROJECT_ID=control-financiero-test
# ... credenciales de testing
```

### Production (Variables de Sistema)
```bash
# En servidor de producción
export MONGODB_URI=mongodb+srv://prod_user:prod_pass@...
export FIREBASE_PROJECT_ID=control-financiero-prod
# ... configurar via GitHub Secrets o CI/CD
```

---

## ✅ Checklist de Configuración

- [ ] Crear archivo `.env` en raíz del proyecto
- [ ] Copiar valores de MongoDB Atlas
- [ ] Descargar `serviceAccountKey.json` de Firebase
- [ ] Copiar valores de Firebase al `.env`
- [ ] Verificar que `.env` está en `.gitignore`
- [ ] Prueba: `gradle bootRun` debería conectar a MongoDB
- [ ] Prueba: Endpoints de API responden correctamente
- [ ] Documentar valores secretos en lugar seguro (gestor de contraseñas)

---

## 🐛 Troubleshooting

### Error: "Cannot connect to MongoDB"

```
Solución:
1. Verificar MONGODB_URI está correcta
2. Verificar credenciales usuario:password
3. Verificar IP está en whitelist de MongoDB Atlas
4. Verificar nombre de base de datos (control-financiero)
```

### Error: "Firebase authentication failed"

```
Solución:
1. Verificar FIREBASE_PRIVATE_KEY tiene saltos de línea correctos
2. Verificar FIREBASE_PROJECT_ID es correcto
3. Verificar serviceAccountKey.json no expiró
4. Descargar nueva clave en Firebase Console
```

### Error: "Variables de entorno no se cargan"

```
Solución:
1. Verificar que spring-dotenv está en dependencies
2. Reiniciar la aplicación
3. Verificar archivo `.env` está en raíz del proyecto
4. Verificar no hay espacios en blanco extraños
```

---

## 📌 Notas Importantes

### Para Desarrolladores
- ✅ Copiar `.env.example` → `.env`
- ✅ Llenar variables con credenciales reales
- ✅ NUNCA subir `.env` a Git
- ✅ Guardar `serviceAccountKey.json` en lugar seguro (Password manager)

### Para DevOps/Producción
- ✅ Usar GitHub Secrets para CI/CD
- ✅ Usar variables de entorno del servidor
- ✅ Usar gestor de secretos (Vault, AWS Secrets Manager, etc.)
- ✅ Rotación periódica de credenciales
- ✅ Auditoría de acceso a secretos

### Para Git
- ✅ `.env` está en `.gitignore` ✓
- ✅ `.env.example` SÍ está versionado ✓
- ✅ Credenciales nunca se suben a GitHub ✓

---

## 🔗 Referencias

- [MongoDB Atlas Docs](https://docs.atlas.mongodb.com/)
- [Firebase Admin SDK Docs](https://firebase.google.com/docs/admin/setup)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [dotenv-java](https://github.com/cdimascio/dotenv-java)

---

**Guía completada**: 16 Octubre 2025  
**Status**: ✅ Configuración lista
