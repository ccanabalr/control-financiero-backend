# 🔍 CHECKLIST RÁPIDO: ¿ES ACCESIBLE TU MONGODB ATLAS?

## 🚨 IMPORTANTE: Verifícalo AHORA

Tu URL podría no funcionar por 3 razones principales:

---

## ❌ Razón 1: Usuario o contraseña incorrecta

**En MongoDB Atlas:**
1. https://cloud.mongodb.com/ → inicia sesión
2. **Database Access** (menú izquierdo)
3. Busca: `ccanabalr_db_user`
4. ¿Está ACTIVE? (debe estar verde)
5. Si expiró: haz clic en (...) → **Edit Password**

**Si necesitas resetear contraseña:**
- Copia la nueva contraseña
- Actualiza en Render: `MONGODB_URI`

---

## ❌ Razón 2: IP no permitida

**En MongoDB Atlas:**
1. https://cloud.mongodb.com/ → inicia sesión
2. **Network Access** (menú izquierdo)
3. ¿Ves `0.0.0.0/0`? (Allow access from anywhere)
4. ¿Está ACTIVE? (debe estar verde)

**Si NO está:**
1. Haz clic: **"Add IP Address"**
2. Selecciona: **"Allow Access from Anywhere"** (0.0.0.0/0)
3. Haz clic: **"Confirm"**

---

## ❌ Razón 3: Base de datos no especificada

**Tu URL actual:**
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/?retryWrites=true&w=majority
```

**Debe ser:**
```
mongodb+srv://ccanabalr_db_user:ptELeKry8mbUCFJw@cluster0.jahbc3i.mongodb.net/control_financiero?retryWrites=true&w=majority
                                                     ↑
                                                Falta esto
```

---

## 🧪 PRUEBA RÁPIDA

### Ejecuta este script en PowerShell:

```powershell
cd "c:\Users\CARLOS\Documents\personal\personal\control-financiero-backend"
.\verificar-mongodb.ps1
```

Te dirá si tu URL es válida.

---

## 📋 CHECKLIST DE MONGODB ATLAS

Antes de actualizar en Render, verifica:

### Database Access:
- [ ] Usuario `ccanabalr_db_user` existe
- [ ] Status es **ACTIVE** (verde)
- [ ] Contraseña es: `ptELeKry8mbUCFJw`

### Network Access:
- [ ] `0.0.0.0/0` existe
- [ ] Status es **ACTIVE** (verde)

### Cluster:
- [ ] Cluster0 está disponible (verde)
- [ ] Está en la región correcta

### Connection String:
- [ ] Comienza con: `mongodb+srv://`
- [ ] Incluye: `/control_financiero`
- [ ] Incluye: `?retryWrites=true&w=majority`

---

## 🚀 SI TODO ESTÁ BIEN

Entonces tu MongoDB Atlas **ES accesible**.

En ese caso:
1. Actualiza `MONGODB_URI` en Render
2. Render hace redeploy automático
3. Debe funcionar ✅

---

## 🆘 SI ALGO FALLA

Escribe exactamente qué error ves:

**Ejemplo 1:**
```
Exception authenticating MongoCredential
```
→ Contraseña incorrecta

**Ejemplo 2:**
```
SocketTimeoutException: connection timed out
```
→ IP no permitida o Network Access desactivado

**Ejemplo 3:**
```
Database name must not be empty
```
→ Falta `/control_financiero` en la URL

---

**¿Revisaste todo en MongoDB Atlas? ¿Qué ves?**