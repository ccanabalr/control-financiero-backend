# 📊 ÁRBOL DE DECISIÓN: POSIBLES PROBLEMAS

## Si hay conexión a MongoDB pero el app no arranca

```
¿Ves "MongoClient with metadata created"?
│
├─ SÍ, luego ¿qué error ves?
│
├─ ESCENARIO 1: "No active profile set, falling back to 1 default profile: default"
│  └─ SOLUCIÓN: Agrega SPRING_PROFILES_ACTIVE=production en Render
│     └─ Redeploy automático
│
├─ ESCENARIO 2: "Cannot resolve reference to bean 'mongoTemplate'"
│  └─ SOLUCIÓN: El bean no se está creando
│     └─ Verifica que build.gradle tiene spring-boot-starter-data-mongodb
│     └─ Redeploy
│
├─ ESCENARIO 3: "NoClassDefFoundError: ch/qos/logback/classic/spi/ThrowableProxy"
│  └─ SOLUCIÓN: Ya corregido en código
│     └─ Redeploy para usar nuevo Dockerfile
│
├─ ESCENARIO 4: "Started ControlFinancieroAplicacion" ✅
│  └─ ÉXITO: El app arrancó
│     └─ Prueba: curl https://control-financiero-backend.onrender.com/api/health
│
└─ NO, no ves "MongoClient"
   └─ El problema es Network Access en MongoDB Atlas
      └─ Ve a mongodb.com → Network Access → Verifica 0.0.0.0/0
```

---

## 🔍 LOS 4 ESCENARIOS MÁS PROBABLES

### **ESCENARIO 1: Profile no activado**

**Qué ves:**
```
2025-10-16T21:45:17.633Z  INFO ... No active profile set, 
falling back to 1 default profile: "default"
```

**¿Por qué?** `SPRING_PROFILES_ACTIVE` no está en Render

**SOLUCIÓN RÁPIDA:**
1. Render Dashboard → Tu servicio
2. Environment → Add Variable
3. Key: `SPRING_PROFILES_ACTIVE`
4. Value: `production`
5. Save → Redeploy automático

**Después verás:**
```
The following 1 profile is active: "production"
```

---

### **ESCENARIO 2: mongoTemplate no se crea**

**Qué ves:**
```
Cannot resolve reference to bean 'mongoTemplate' 
while setting bean property 'mongoOperations'
```

**¿Por qué?** Spring Data MongoDB no tiene configuración correcta

**SOLUCIÓN:**
1. Ya está en build.gradle: `spring-boot-starter-data-mongodb`
2. Hacer redeploy para usar nuevo código

---

### **ESCENARIO 3: Logback no encuentra clases**

**Qué ves:**
```
NoClassDefFoundError: ch/qos/logback/classic/spi/ThrowableProxy
```

**¿Por qué?** Logback no está empaquetado en el JAR

**SOLUCIÓN:**
1. Ya está en build.gradle:
   ```
   org.springframework.boot:spring-boot-starter-logging
   ch.qos.logback:logback-classic:1.4.14
   ch.qos.logback:logback-core:1.4.14
   ```
2. Hacer redeploy para compilar con nuevas dependencias

---

### **ESCENARIO 4: ¡ÉXITO! El app arrancó**

**Qué ves:**
```
2025-10-16T21:45:42.833Z  INFO 1 --- [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-10-16T21:45:44.636Z  INFO 1 --- [main] o.a.c.c.C.[Tomcat].[localhost].[/api]    : Initializing Spring embedded WebApplicationContext
2025-10-16T21:45:52.232Z  INFO 1 --- [main] org.mongodb.driver.client                : MongoClient with metadata created
2025-10-16T21:45:53.129Z  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080
2025-10-16T21:45:54.038Z  INFO 1 --- [main] c.proyecto.ControlFinancieroAplicacion   : Started ControlFinancieroAplicacion in 36.405s
```

**¿Qué hacer?**
1. Prueba un endpoint:
   ```
   curl https://control-financiero-backend.onrender.com/api/health
   ```

2. Deberías recibir respuesta ✅

---

## 📝 CHECKLIST PARA CADA ESCENARIO

### Si ves "No active profile set":
- [ ] Agrega `SPRING_PROFILES_ACTIVE` en Render
- [ ] Save
- [ ] Espera redeploy
- [ ] Verifica logs nuevamente

### Si ves "Cannot resolve reference to bean 'mongoTemplate'":
- [ ] Verifica que build.gradle tiene `spring-boot-starter-data-mongodb`
- [ ] Si no está, aggrega
- [ ] Commit y push
- [ ] Render redeploy automático
- [ ] Verifica logs

### Si ves "NoClassDefFoundError: logback":
- [ ] Verifica que build.gradle tiene `spring-boot-starter-logging`
- [ ] Si no está, aggrega
- [ ] Commit y push
- [ ] Render redeploy automático
- [ ] Verifica logs

### Si ves "Started ControlFinancieroAplicacion":
- [ ] ¡ÉXITO! El app arrancó
- [ ] Prueba el endpoint: `/api/health`
- [ ] Si responde → ¡FUNCIONA! ✅

---

## 🆘 ¿CUÁL ES TU CASO?

**Dime qué ves exactamente en los logs después de "MongoClient with metadata created":**

1. ¿"No active profile set"?
2. ¿"Cannot resolve reference"?
3. ¿"NoClassDefFoundError"?
4. ¿"Started ControlFinancieroAplicacion"?
5. ¿Otro error? (cópialo exacto)