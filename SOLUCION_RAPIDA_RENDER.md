# 🚀 SOLUCIÓN RÁPIDA - CONFIGURAR RENDER

## 🔴 Problema

```
Database name must not be empty
```

## ✅ Solución en 3 pasos

### **PASO 1: Ve a Render Dashboard**
```
https://dashboard.render.com
 ↓
Tu servicio: control-financiero-backend
 ↓
Menú izquierdo → Environment
```

### **PASO 2: Agrega esta variable OBLIGATORIA**

**Key:** `MONGODB_URI`

**Value:** (reemplaza tu información)
```
mongodb+srv://usuario:password@cluster.mongodb.net/control_financiero?retryWrites=true&w=majority
```

### **PASO 3: Agrega esta variable OBLIGATORIA**

**Key:** `SPRING_PROFILES_ACTIVE`

**Value:**
```
production
```

### **PASO 4: Haz clic en "Save"**

✅ **Render automáticamente hace redeploy**

---

## 🎯 ¿Dónde obtengo MONGODB_URI?

### **Opción A: Desde MongoDB Atlas (RECOMENDADO)**

1. https://cloud.mongodb.com/
2. Tu cluster → **Connect**
3. **Drivers** → Copia connection string
4. Reemplaza usuario y contraseña
5. Asegúrate que termina con `/control_financiero`

**Ejemplo:**
```
mongodb+srv://carlos:mipwd@cluster0.xyz.mongodb.net/control_financiero?retryWrites=true&w=majority
```

### **Opción B: Desde tu .env local (si lo tienes)**

Busca en tu archivo `.env`:
```
MONGODB_URI=mongodb+srv://...
```

---

## ⏱️ Timeline

```
Guardas variables en Render
    ↓
Render detecta cambios (automático)
    ↓
Inicia nuevo deploy (~1 min)
    ↓
Descarga dependencias (~2 min)
    ↓
Compila código (~5 min)
    ↓
Crea Docker (~2 min)
    ↓
¡Listo! API disponible ✅ (~10 min total)
```

---

## 🔍 Verificar que funciona

En Render, sección **Logs**, deberías ver:

```
✅ Application 'ControlFinancieroAplicacion' started successfully
✅ Server started on port(s): 8080 with context path '/api'
✅ Started in 15.234 seconds
✅ Live! Your service is live at https://control-financiero-backend.onrender.com
```

---

## ⚠️ Errores comunes

### Error: `MONGODB_URI not found`
→ Verifica que la escribiste exactamente: `MONGODB_URI` (mayúsculas)

### Error: `Connection refused`
→ Tu URL de MongoDB está mal
→ Verifica usuario y contraseña

### Error: `Authentication failed`
→ Usuario o contraseña de MongoDB son incorrectos
→ O MongoDB Atlas no permite esa IP

---

## 🆘 Si algo sigue fallando

1. **Verifica logs en Render** (sección Logs)
2. **Copia el error exacto**
3. **Comprueba MONGODB_URI** (usuario, password, cluster name)
4. **Verifica que MongoDB Atlas permite cualquier IP** (0.0.0.0/0)

---

**¿Ya agregaste las variables? ¿Qué ves en los logs?**