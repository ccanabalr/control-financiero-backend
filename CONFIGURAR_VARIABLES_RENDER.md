# 🔧 CONFIGURACIÓN DE VARIABLES DE ENTORNO EN RENDER

## 🚨 Problema detectado

```
org.springframework.beans.BeanInstantiationException: 
Database name must not be empty
```

**Causa:** Las variables de entorno no están configuradas en Render.

---

## ✅ Solución: Configurar variables en Render Dashboard

### **Paso 1: Ir al dashboard de Render**

1. Ve a: https://dashboard.render.com
2. Busca tu servicio: `control-financiero-backend`
3. Haz clic en el servicio

### **Paso 2: Ir a "Environment"**

1. En el menú lateral izquierdo, busca: **"Environment"**
2. Haz clic en el botón **"Add Environment Variable"**

### **Paso 3: Agregar MONGODB_URI**

Copia y pega exactamente esto:

**Key:** 
```
MONGODB_URI
```

**Value:** 
```
mongodb+srv://usuario:password@cluster.mongodb.net/control_financiero?retryWrites=true&w=majority
```

**Reemplaza:**
- `usuario` → Tu usuario de MongoDB Atlas
- `password` → Tu contraseña de MongoDB Atlas
- `cluster` → Tu nombre de cluster de MongoDB Atlas

**Ejemplo real:**
```
mongodb+srv://carlos:miPassword123@cluster0.abcd1234.mongodb.net/control_financiero?retryWrites=true&w=majority
```

---

## 📋 Variables de entorno recomendadas

Agrega todas estas en Render:

### **1. MongoDB (OBLIGATORIO)**
```
Key: MONGODB_URI
Value: mongodb+srv://usuario:password@cluster.mongodb.net/control_financiero?retryWrites=true&w=majority
```

### **2. Spring Boot Profile (OBLIGATORIO)**
```
Key: SPRING_PROFILES_ACTIVE
Value: production
```

### **3. Base de datos (OPCIONAL - con valor por defecto)**
```
Key: SPRING_DATA_MONGODB_DATABASE
Value: control_financiero
```

### **4. Firebase Project ID (SI USAS FIREBASE)**
```
Key: FIREBASE_PROJECT_ID
Value: tu-proyecto-firebase-id
```

### **5. Firebase Private Key ID**
```
Key: FIREBASE_PRIVATE_KEY_ID
Value: abc123def456...
```

### **6. Firebase Private Key (IMPORTANTE)**
```
Key: FIREBASE_PRIVATE_KEY
Value: -----BEGIN PRIVATE KEY-----\nTU_CLAVE_PRIVADA_AQUI\n-----END PRIVATE KEY-----\n
```

⚠️ **NOTA:** La clave debe incluir `\n` para saltos de línea

### **7. Firebase Client Email**
```
Key: FIREBASE_CLIENT_EMAIL
Value: firebase-adminsdk-xxxxx@tu-proyecto.iam.gserviceaccount.com
```

### **8. Firebase Client ID**
```
Key: FIREBASE_CLIENT_ID
Value: 123456789012345678901
```

### **9. Firebase Client X509 Cert URL**
```
Key: FIREBASE_CLIENT_X509_CERT_URL
Value: https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-xxxxx%40tu-proyecto.iam.gserviceaccount.com
```

### **10. CORS (OBLIGATORIO)**
```
Key: ALLOWED_ORIGINS
Value: https://tu-app.web.app,https://tu-app.firebaseapp.com
```

---

## 🔑 ¿De dónde obtengo MONGODB_URI?

### **Si tienes MongoDB Atlas:**

1. Ve a: https://cloud.mongodb.com/
2. Inicia sesión
3. Selecciona tu cluster
4. Haz clic en **"Connect"**
5. Selecciona **"Drivers"**
6. Copia la connection string
7. Reemplaza `<username>` y `<password>` con tus credenciales
8. Asegúrate que el nombre de la base de datos es: `control_financiero`

**Formato final:**
```
mongodb+srv://usuario:password@cluster.mongodb.net/control_financiero?retryWrites=true&w=majority
```

---

## 🔐 ¿De dónde obtengo credenciales de Firebase?

### **Si usas Firebase Admin SDK:**

1. Ve a: https://console.firebase.google.com/
2. Selecciona tu proyecto
3. **Project Settings** (rueda de engranaje)
4. Pestaña: **"Service Accounts"**
5. Haz clic en **"Generate New Private Key"**
6. Se descargará un JSON con tus credenciales
7. Copia los valores:
   - `project_id` → FIREBASE_PROJECT_ID
   - `private_key_id` → FIREBASE_PRIVATE_KEY_ID
   - `private_key` → FIREBASE_PRIVATE_KEY
   - `client_email` → FIREBASE_CLIENT_EMAIL
   - `client_id` → FIREBASE_CLIENT_ID
   - `client_x509_cert_url` → FIREBASE_CLIENT_X509_CERT_URL

---

## ✅ Checklist

- [ ] Accedí a https://dashboard.render.com
- [ ] Encontré mi servicio `control-financiero-backend`
- [ ] Fui a "Environment"
- [ ] Agregué `MONGODB_URI` con mi connection string
- [ ] Agregué `SPRING_PROFILES_ACTIVE` = `production`
- [ ] Guardé los cambios
- [ ] Render automáticamente hace redeploy

---

## 🚀 Después de agregar variables

1. **Render automáticamente hará redeploy**
2. **Espera ~5 minutos**
3. **Verifica en los logs que arranca sin errores**
4. **Busca:** `Started ControlFinancieroAplicacion` ✅

---

## 🔍 Verificar en los logs

Deberías ver:

```
✅ Application 'ControlFinancieroAplicacion' started successfully
✅ Server is listening on port 8080
✅ MongoDB connection established
✅ Live! Your service is live at https://control-financiero-backend.onrender.com
```

---

## ❓ Preguntas comunes

### P: ¿Las variables de entorno son sensibles a mayúsculas?
R: **SÍ**, deben coincidir exactamente.

### P: ¿Puedo agregar variables después de crear el servicio?
R: **SÍ, perfectamente**. Render automáticamente redeploy.

### P: ¿Dónde veo si algo está mal?
R: En la sección **"Logs"** del servicio en Render Dashboard.

### P: ¿Puedo cambiar MONGODB_URI después?
R: **SÍ**, Render automáticamente redeploy y toma la nueva URI.

---

## ⚠️ Notas importantes

1. **NUNCA hardcodees credenciales en el código**
2. **Usa variables de entorno siempre**
3. **MongoDB Atlas debe permitir conexiones desde cualquier IP** (0.0.0.0/0) en el plan gratuito
4. **Las claves privadas de Firebase incluyen saltos de línea especiales** (`\n`)

---

## 🎯 Próximo paso

1. **Agrega las variables en Render**
2. **Guarda**
3. **Espera a que redeploy automáticamente**
4. **Verifica en los logs que todo funcionó** ✅

**¿Ya agregaste las variables en Render?**