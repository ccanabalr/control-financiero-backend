# 🎯 DIAGNÓSTICO: ¿POR QUÉ FALLA MONGODB?

## 🔴 Tu sospecha es correcta

Probablemente el problema es que **MongoDB Atlas no es accesible desde Render** o **la URL está incorrecta**.

---

## 🔍 LOS 3 PROBLEMAS MÁS COMUNES

### **Problema 1: Base de datos no especificada en la URL** ⭐ PROBABLE

**Síntoma en logs:**
```
Database name must not be empty
```

**Tu URL tiene:**
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/?retryWrites=true&w=majority
                                                                              ↑ /?
```

**Debe tener:**
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority
                                                                              ↑ /control_financiero?
```

**Solución:** Actualiza `MONGODB_URI` en Render con la URL correcta ✅

---

### **Problema 2: Network Access no permite conexiones desde Render**

**Síntoma en logs:**
```
SocketTimeoutException: connection timed out
```

**En MongoDB Atlas:**
1. **Network Access** (menú izquierdo)
2. ¿Ves `0.0.0.0/0` en VERDE?
   - SÍ → Está bien
   - NO → Agrégalo ahora

---

### **Problema 3: Contraseña incorrecta o expirada**

**Síntoma en logs:**
```
Exception authenticating MongoCredential
```

**En MongoDB Atlas:**
1. **Database Access** (menú izquierdo)
2. Busca: `ccanabalr_db_user`
3. ¿Está ACTIVE (en verde)?
   - SÍ → Verifica contraseña
   - NO → Haz clic en (...) → Edit Password

---

## 🧪 VERIFICA AHORA

### **Paso 1: Ejecuta el script de diagnóstico**

```powershell
cd "c:\Users\CARLOS\Documents\personal\personal\control-financiero-backend"
.\verificar-mongodb.ps1
```

Te dirá si la URL está bien formada.

### **Paso 2: Revisa MongoDB Atlas**

```
https://cloud.mongodb.com/
↓
Cluster0 → Selecciona
↓
Network Access → Verifica que 0.0.0.0/0 está ACTIVE
↓
Database Access → Verifica que ccanabalr_db_user está ACTIVE
```

### **Paso 3: Prueba localmente**

```powershell
cd "c:\Users\CARLOS\Documents\personal\personal\control-financiero-backend"
.\gradlew bootRun
```

Busca en los logs:
- ✅ `MongoClient with metadata created`
- ✅ `Adding discovered server`

Si funciona localmente pero NO en Render → **Es un problema de Network Access**

---

## 📋 DIAGNÓSTICO RÁPIDO

**¿Qué ves en Render logs?**

| Error | Significado | Solución |
|-------|-------------|----------|
| `Database name must not be empty` | Falta `/control_financiero` | Actualiza URL |
| `SocketTimeoutException` | IP no permitida | Agrega 0.0.0.0/0 a Network Access |
| `Exception authenticating` | Contraseña incorrecta | Verifica credenciales |
| `NoClassDefFoundError: logback` | Dependencias mal empaquetadas | Ya corregido en código |

---

## ✅ QUÉ HACER AHORA

### **Opción A: Si la URL necesita /control_financiero**

1. Copia la URL correcta:
   ```
   mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority
   ```

2. En Render Dashboard:
   - Environment → MONGODB_URI
   - Reemplaza con URL correcta
   - Save → Redeploy automático

### **Opción B: Si no funciona localmente**

1. Ejecuta:
   ```powershell
   .\gradlew bootRun
   ```

2. Copia exactamente el error que ves

3. Comparte conmigo qué error específico ves

---

## 🎯 PLAN DE ACCIÓN

```
1. Ejecutar verificar-mongodb.ps1
   ↓
2. Ver qué problema identifica
   ↓
3. Revisar MongoDB Atlas según el problema
   ↓
4. Actualizar MONGODB_URI en Render
   ↓
5. Esperar redeploy
   ↓
6. Verificar logs
```

---

**¿Ejecutaste el script `verificar-mongodb.ps1`? ¿Qué dice?**

O cuéntame exactamente:
- ¿Qué error ves en Render logs?
- ¿Qué ves en MongoDB Atlas?