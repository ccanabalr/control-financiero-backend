# 🚀 DESPLIEGUE CONTROL FINANCIERO EN RENDER

## ⚡ Guía rápida (5 minutos)

### 1. Prepara el backend
```bash
# Desde la carpeta backend
.\test-local.ps1  # Windows
# o
bash test-local.sh  # Mac/Linux
```

### 2. Sube a GitHub
```bash
git add .
git commit -m "chore: ready for render deployment"
git push -u origin master
```

### 3. Ve a Render
- Ve a https://render.com
- Haz login con GitHub
- Crea nuevo **Web Service**
- Conecta tu repositorio `control-financiero-backend`

### 4. Configura el servicio
| Campo | Valor |
|-------|-------|
| Build Command | `./gradlew build -x test` |
| Start Command | `java -Dserver.port=$PORT -jar build/libs/control-financiero-backend-1.0.0.jar` |

### 5. Agrega variables de entorno
Ve al archivo `VARIABLES_ENTORNO_RENDER_PLANTILLA.md` para los valores exactos

### 6. ¡Listo!
Render iniciará automáticamente la construcción y despliegue

---

## 📚 Documentación completa

Para una guía paso a paso detallada, ve a:
- **`RENDER_CONFIGURACION_PASO_A_PASO.md`** - Guía completa con capturas
- **`VARIABLES_ENTORNO_RENDER_PLANTILLA.md`** - Variables de entorno necesarias
- **`GUIA_DESPLIEGUE_COMPLETA.md`** - Información general de despliegue

---

## 🔗 Enlaces útiles

- [Render Dashboard](https://render.com/dashboard)
- [MongoDB Atlas](https://cloud.mongodb.com)
- [Firebase Console](https://console.firebase.google.com)
- [GitHub](https://github.com/ccanabalr)

---

## 🆘 ¿Problemas?

### El build falla
1. Ejecuta `./gradlew build -x test` localmente
2. Revisa los logs en Render
3. Verifica las variables de entorno

### Error de MongoDB
1. Asegúrate de que la URI es correcta
2. Agrega IP `0.0.0.0/0` en MongoDB Atlas Network Access
3. Espera 5 minutos a que se propague

### Error de Firebase
1. Descarga nuevamente el JSON de credenciales
2. Reemplaza los valores de `FIREBASE_*`
3. Verifica que `FIREBASE_PRIVATE_KEY` esté entre comillas

---

## ✅ Checklist de despliegue

- [ ] `test-local.ps1` o `test-local.sh` pasan todas las pruebas
- [ ] Código pusheado a GitHub en rama `master`
- [ ] Cuenta Render creada y conectada a GitHub
- [ ] Web Service creado en Render
- [ ] Variables de entorno configuradas
- [ ] Build completado sin errores
- [ ] Servicio está "Live"
- [ ] URL pública anotada

---

¡Felicidades! Tu backend está en la nube. 🎉