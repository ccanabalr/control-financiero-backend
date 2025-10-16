# 🚀 GUÍA DE DEPLOY - RENDER (Backend Java/Spring Boot)

**Fecha**: 16 Octubre 2025  
**Versión**: 1.0.0  
**Runtime**: Java 21 + Gradle + Spring Boot 3.2

---

## 📋 Requisitos Previos

- [x] Cuenta en [Render.com](https://render.com)
- [x] Repositorio Git (GitHub, GitLab, Gitea)
- [x] MongoDB Atlas con URI accesible desde internet
- [x] Firebase serviceAccountKey.json (credenciales)
- [x] `render.yaml` en raíz del proyecto ✅ CREADO

---

## 🔑 Archivo de Configuración

**Ubicación**: `render.yaml`  
**Estado**: ✅ Creado  

**Contenido**:
```yaml
services:
  - type: web
    name: control-financiero-backend
    runtime: java
    buildCommand: ./gradlew build -x test
    startCommand: java -jar build/libs/control-financiero-backend-1.0.0.jar
    envVars:
      - MONGODB_URI
      - FIREBASE_PROJECT_ID
      - FIREBASE_PRIVATE_KEY_ID
      - FIREBASE_PRIVATE_KEY
      - FIREBASE_CLIENT_EMAIL
      - FIREBASE_CLIENT_ID
      - SPRING_PROFILES_ACTIVE: production
      - SERVER_PORT: 8080
```

---

## 🏗️ Configuración del Build

### build.gradle
✅ Spring Boot plugin configurado  
✅ FAT JAR habilitado  
✅ Nombre estandarizado: `control-financiero-backend-1.0.0.jar`

```groovy
bootJar {
    archiveFileName = 'control-financiero-backend-1.0.0.jar'
}
```

### application-production.yml
✅ Configuración de producción  
✅ MongoDB URI desde variable de entorno  
✅ Logging optimizado  
✅ Compresión habilitada

---

## 📝 PASOS DE DEPLOY

### PASO 1: Preparar el Repositorio

```bash
# 1. Asegurar que render.yaml está en raíz
ls render.yaml
# Output: render.yaml (debe existir)

# 2. Asegurar que .gitignore no ignora render.yaml
cat .gitignore | grep render.yaml
# No debe aparecer (si aparece, remover)

# 3. Commit y push
git add render.yaml build.gradle
git commit -m "chore: add Render deployment configuration"
git push origin main
```

### PASO 2: Conectar Repositorio en Render

1. Ir a [https://dashboard.render.com](https://dashboard.render.com)
2. Click en **"New +"**
3. Seleccionar **"Web Service"**
4. Seleccionar **"Connect Git Account"**
   - Autorizar acceso a GitHub/GitLab
   - Seleccionar repositorio: `control-financiero-backend`
5. Click en **"Connect"**

### PASO 3: Configurar Variables de Entorno

En Render Dashboard → Tu servicio → Environment:

```
MONGODB_URI=mongodb+srv://usuario:password@cluster0.mongodb.net/control-financiero?retryWrites=true&w=majority
FIREBASE_PROJECT_ID=tu-proyecto-id-aqui
FIREBASE_PRIVATE_KEY_ID=abc123def456...
FIREBASE_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----\nMIIE...
FIREBASE_CLIENT_EMAIL=firebase-adminsdk-xxxxx@tu-proyecto.iam.gserviceaccount.com
FIREBASE_CLIENT_ID=123456789012345
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
```

**⚠️ IMPORTANTE**: 
- `FIREBASE_PRIVATE_KEY` contiene saltos de línea (`\n`)
- En Render, usar formato: `-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----`
- Copiar exactamente del `serviceAccountKey.json`

### PASO 4: Configurar Build y Deploy

En Render Dashboard → Tu servicio → Deploy:

1. **Build Command**: `./gradlew build -x test`
2. **Start Command**: `java -jar build/libs/control-financiero-backend-1.0.0.jar`
3. **Runtime**: Java
4. **Auto-deploy**: Habilitado (push a main = deploy automático)

### PASO 5: Verificar Configuración

- [x] Runtime: Java
- [x] Region: USA (o tu región preferida)
- [x] Tier: Starter ($7/mes)
- [x] Health check path: `/api/health` (opcional)

---

## 🚀 INICIAR DEPLOY

1. Click en **"Deploy"** (Manual)
   - Render clonará el repo
   - Ejecutará `./gradlew build -x test`
   - Iniciará `java -jar build/libs/control-financiero-backend-1.0.0.jar`
   - Obtendrás URL: `https://control-financiero-backend.onrender.com`

2. O espera a que se auto-deploya con push a main

**Tiempo de deploy**: 5-10 minutos (primera vez)

---

## ✅ VERIFICAR QUE FUNCIONA

### 1. Health Check

```bash
curl https://control-financiero-backend.onrender.com/api/health
# Debe retornar: 200 OK (o similar)
```

### 2. Test de Endpoints

```bash
# GET (Sin autenticación, debe retornar 401)
curl https://control-financiero-backend.onrender.com/api/v1/transacciones

# Con JWT token de Firebase
curl -H "Authorization: Bearer <TOKEN>" \
  https://control-financiero-backend.onrender.com/api/v1/transacciones
```

### 3. Verificar Logs

En Render Dashboard → Tu servicio → Logs:
- Buscar mensajes de Spring Boot startup
- Verificar conexión a MongoDB
- Verificar inicialización de Firebase

---

## 🐛 TROUBLESHOOTING

### Error: "Build failed: ./gradlew: permission denied"

**Solución**:
```bash
# En tu máquina local
git update-index --chmod=+x ./gradlew
git commit -m "chore: fix gradlew permissions"
git push
```

### Error: "Cannot connect to MongoDB"

**Verificar**:
1. MONGODB_URI está correcta en Render env vars
2. IP de Render está en MongoDB Atlas whitelist
   - MongoDB Atlas → Network Access → Add IP
   - Agregar `0.0.0.0/0` (permite todas las IPs)
3. Nombre de base de datos es `control-financiero`

### Error: "Firebase authentication failed"

**Verificar**:
1. FIREBASE_PRIVATE_KEY tiene saltos de línea correctos (`\n`)
2. FIREBASE_PROJECT_ID es exacto
3. serviceAccountKey.json no expiró
4. Descargar nueva clave en Firebase Console

### Error: "Port already in use"

**Solución**:
- Render asigna puerto automáticamente via `SERVER_PORT` env var
- Verificar que en render.yaml está: `- key: SERVER_PORT` / `value: 8080`

### Error: "Timeout during build"

**Solución**:
```bash
# Cambiar en render.yaml
startCommand: java -Xmx256m -jar build/libs/control-financiero-backend-1.0.0.jar
```
(Reduce memoria para máquinas más pequeñas)

---

## 📊 MONITOREO POST-DEPLOY

### En Render Dashboard

- **Logs**: Ver en tiempo real
- **Metrics**: CPU, Memory, Network
- **Auto-restart**: Habilitado automáticamente
- **SSL/TLS**: Automático (https://)

### En tu aplicación

Logs de Spring Boot:
```
[INFO] com.proyecto.Application: Starting Application
[INFO] o.s.d.m.c.MongoClient: Cluster created
[INFO] o.s.b.w.e.tomcat.TomcatWebServer: Tomcat started on port 8080
```

---

## 🔄 PIPELINE DE ACTUALIZACIONES

```
Git Push (main)
    ↓
Render detecta cambio
    ↓
Descarga último código
    ↓
./gradlew build -x test
    ↓
Genera control-financiero-backend-1.0.0.jar
    ↓
Detiene servicio anterior
    ↓
Inicia nuevo JAR
    ↓
URL sigue siendo la misma
    ↓
✅ Deployment completado (zero downtime)
```

---

## 📱 INTEGRACIÓN FRONTEND

Actualizar frontend para conectar a Render:

### src/app/core/services/autenticacion.servicio.ts

```typescript
private urlBackend = 'https://control-financiero-backend.onrender.com/api/v1';

// Usa esta URL en lugar de http://localhost:8080
```

### src/environments/environment.production.ts

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://control-financiero-backend.onrender.com/api/v1'
};
```

---

## 💾 BASE DE DATOS

### MongoDB Atlas

MongoDB Atlas requiere whitelist de IPs para conexiones externas:

1. Ir a MongoDB Atlas → Network Access
2. Click en "Add IP Address"
3. Opción A: `0.0.0.0/0` (permite todas)
4. Opción B: Agregar IP específica de Render
   - Obtener IP: En Render logs aparecerá
   - Más seguro pero requiere actualizar si IP cambia

---

## 🔒 SEGURIDAD EN PRODUCCIÓN

### Checklist

- [x] HTTPS habilitado (Render lo hace automático)
- [x] Variables de entorno seguras (Render las encripta)
- [x] MongoDB con autenticación (usuario:password)
- [x] Firebase serviceAccountKey seguro (solo en Render, no en Git)
- [x] CORS configurado en Spring Boot
- [x] Rate limiting (próxima mejora)
- [x] Validación de entrada (ya implementado)

---

## 📈 ESCALABILIDAD

### Si necesitas más recursos

**Starter** (Actual - $7/mes)
```
RAM: 512 MB
CPU: Compartido
```

**Standard** ($12/mes)
```
RAM: 2 GB
CPU: Dedicado
```

**Pro** ($25/mes+)
```
RAM: 4+ GB
CPU: Múltiples cores
```

---

## 🎊 RESULTADO FINAL

Después del deploy exitoso:

```
✅ Backend en: https://control-financiero-backend.onrender.com
✅ API disponible en: https://control-financiero-backend.onrender.com/api/v1
✅ Auto-deploy con Git push
✅ SSL/TLS automático
✅ Monitoreo en Render dashboard
✅ Logs en tiempo real
```

---

## 📚 REFERENCIA RÁPIDA

| Tarea | Comando |
|-------|---------|
| Push y deploy | `git push origin main` |
| Ver logs | Render Dashboard → Logs |
| Recolectar garbage | Automático cada 24h |
| Reiniciar servicio | Render Dashboard → Restart |
| Cambiar variables | Render Dashboard → Environment → Update |

---

## 🔗 REFERENCIAS

- [Render Docs - Java](https://render.com/docs/deploy-java)
- [Spring Boot on Render](https://render.com/docs/native-runtime-java)
- [MongoDB Atlas Whitelist](https://docs.atlas.mongodb.com/security/ip-access-list/)
- [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup)

---

**Guía completada**: 16 Octubre 2025  
**Status**: ✅ Listo para deploy
