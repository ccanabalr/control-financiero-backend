# 🔍 VERIFICAR SI MONGODB ATLAS ES ACCESIBLE

## ✅ Paso 1: Verificar la URL en MongoDB Atlas

### 1.1 Ve a https://cloud.mongodb.com/

### 1.2 Inicia sesión

### 1.3 Selecciona tu cluster: **Cluster0**

### 1.4 Busca el botón **"Connect"** (arriba a la derecha)

---

## 🔗 Paso 2: Obtener la Connection String correcta

### 2.1 En el popup de "Connect", selecciona:
```
Drivers → MongoDB for Java
```

### 2.2 Verás algo como:
```
mongodb+srv://<username>:<password>@cluster0.jahbc3i.mongodb.net/?retryWrites=true&w=majority
```

### 2.3 Reemplaza:
- `<username>` → `ccanabalr_db_user`
- `<password>` → `ptELeKry8mbUCFJw`

### 2.4 **IMPORTANTE**: Agrega el nombre de la base de datos:
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority
```

---

## 🔐 Paso 3: Verificar credenciales en MongoDB Atlas

### 3.1 Ve a **Database Access** (menú izquierdo)

### 3.2 Busca tu usuario: `ccanabalr_db_user`

### 3.3 Verifica:
- ✅ Estado: **ACTIVE** (verde)
- ✅ Contraseña: Debe estar en tu `.env` o notas

### 3.4 Si la contraseña expiró o no es correcta:
- Haz clic en los tres puntos (...) del usuario
- Selecciona **"Edit Password"**
- Genera una nueva contraseña
- **COPIA y guarda** la nueva contraseña

---

## 🌐 Paso 4: Verificar que MongoDB Atlas permite conexiones desde Render

### 4.1 Ve a **Network Access** (menú izquierdo)

### 4.2 Busca la entrada que dice:
```
0.0.0.0/0
```

### 4.3 Verifica:
- ✅ Status: **ACTIVE** (verde)
- ✅ Comment: Debe decir algo como "Allow access from anywhere"

### 4.4 Si NO está, debes agregar:
1. Haz clic en **"Add IP Address"**
2. Selecciona **"Allow Access from Anywhere"** (0.0.0.0/0)
3. Haz clic en **"Confirm"**

⚠️ **NOTA**: El plan gratuito solo permite 0.0.0.0/0

---

## 🧪 Paso 5: Probar la conexión desde tu PC local

### 5.1 Abre PowerShell

### 5.2 Ejecuta:
```powershell
# Verificar que la URL es válida
$uri = "mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority"

Write-Host "URI: $uri"
```

### 5.3 Ejecuta el backend localmente:
```bash
cd control-financiero-backend
./gradlew bootRun
```

### 5.4 En los logs, busca:
```
✅ MongoClient with metadata created
✅ Adding discovered server cluster0.jahbc3i.mongodb.net:27017
```

---

## 📋 Checklist: ¿Cuál es el problema?

### ❓ ¿Qué ves en Render logs?

**Síntoma 1: Error de autenticación**
```
com.mongodb.MongoSecurityException: Exception authenticating MongoCredential
```
**→ Solución:** Verifica usuario/contraseña en MongoDB Atlas

**Síntoma 2: No se puede conectar**
```
java.net.SocketTimeoutException: connection timed out
```
**→ Solución:** Verifica Network Access (0.0.0.0/0)

**Síntoma 3: Base de datos no encontrada**
```
Database name must not be empty
```
**→ Solución:** Falta `/control_financiero` en la URL

**Síntoma 4: Certificado SSL inválido**
```
PKIX path building failed
```
**→ Solución:** Rara, pero verificar firewall/proxy

---

## 🔧 Paso 6: Verifica tu URL está correcta

### Estructura correcta:
```
mongodb+srv://
  ↓
usuario:contraseña
  ↓
@cluster0.jahbc3i.mongodb.net
  ↓
/control_financiero    ← ⭐ BASE DE DATOS
  ↓
?retryWrites=true&w=majority
```

### Tu URL debería verse así:
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority
```

---

## 🚀 Si todo está bien

Entonces la URL es correcta y funciona. En ese caso:

1. **Actualiza en Render**
2. **Redeploy**
3. **Debería funcionar** ✅

---

## 📝 Checklist final

- [ ] Accedí a MongoDB Atlas Dashboard
- [ ] Verifiqué que Cluster0 existe
- [ ] Copié la connection string correcta
- [ ] Reemplacé `<username>` y `<password>`
- [ ] Agregué `/control_financiero` al final
- [ ] Verifiqué que usuario está ACTIVE en Database Access
- [ ] Verifiqué que Network Access permite 0.0.0.0/0
- [ ] La URL está en formato: `mongodb+srv://user:pass@cluster0.xxx.net/control_financiero?retryWrites=true&w=majority`

---

**¿Cuál es el paso en el que te quedas? ¿Qué ves en MongoDB Atlas?**