# Arquitectura del Backend - Control Financiero

## Patrón: Arquitectura Hexagonal (Puertos y Adaptadores)

```
┌─────────────────────────────────────────────────────────────┐
│                    MUNDO EXTERIOR                           │
│  ┌──────────────┐      ┌──────────────┐   ┌──────────────┐ │
│  │  Angular     │      │   Firebase   │   │   MongoDB    │ │
│  │  Frontend    │      │   Auth       │   │   Atlas      │ │
│  └──────────────┘      └──────────────┘   └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
          ↓                    ↓                    ↓
┌─────────────────────────────────────────────────────────────┐
│                    ADAPTADORES (Infraestructura)            │
│  ┌──────────────┐      ┌──────────────┐   ┌──────────────┐ │
│  │ Controllers  │      │ Filtro Auth  │   │ Repositorios │ │
│  │   REST       │      │   Firebase   │   │   MongoDB    │ │
│  └──────────────┘      └──────────────┘   └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
          ↓                    ↓                    ↓
┌─────────────────────────────────────────────────────────────┐
│                 PUERTOS (Interfaces)                        │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         Puerto de Entrada (Casos de Uso)            │  │
│  │    - ServicioGestionTransaccion                      │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │     Puertos de Salida (Persistencia)                │  │
│  │    - RepositorioTransaccion                         │  │
│  │    - RepositorioResumenMensual                      │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────────────────────────┐
│              NÚCLEO (Lógica de Negocio)                     │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Modelos de Dominio (POJOs)                          │  │
│  │  - Usuario, Cuenta, Categoria                       │  │
│  │  - Transaccion, ResumenMensual                      │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Servicios de Aplicación                             │  │
│  │  - Gestión de Transacciones                         │  │
│  │  - Cálculo de Resúmenes                            │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Flujo de una Petición HTTP

```
1. Cliente Angular
   ↓
2. Envía: POST /api/transacciones
   Header: Authorization: Bearer <JWT_FIREBASE>
   ↓
3. FiltroAutenticacionFirebase
   - Extrae el token
   - Valida con Firebase
   - Establece SecurityContextHolder
   ↓
4. ControladorTransaccion
   - Valida autenticación
   - Extrae UID del usuario
   ↓
5. ServicioGestionTransaccion (Interfaz - Puerto de Entrada)
   ↓
6. ServicioGestionTransaccionImpl (Implementación - Aplicación)
   - Aplica lógica de negocio
   - Valida reglas de negocio
   ↓
7. RepositorioTransaccion (Interfaz - Puerto de Salida)
   ↓
8. AdaptadorPersistenciaTransaccion (Implementación - Infraestructura)
   ↓
9. MongoRepository + MongoTemplate
   - Guarda en MongoDB
   - Actualiza ResumenMensual (UPSERT atómico)
   ↓
10. MongoDB Atlas
    - Persiste datos
    ↓
11. Respuesta JSON
    - Controllers → Angular Frontend
```

## Capas del Proyecto

### 1. **Capa de Dominio** (`dominio/`)
**Responsabilidad**: Contiene la lógica de negocio pura.

- **`modelo/`**: Entidades puras (POJOs) sin dependencias de frameworks
  - No conoce sobre Spring, MongoDB, HTTP, etc.
  - Directamente mapeadas a documentos MongoDB con `@Document` y `@Field`

- **`puertos/`**: Interfaces que definen contratos
  - **`entrada/`**: Casos de uso (que el mundo exterior quiere hacer)
  - **`salida/`**: Abstracciones de persistencia (que el dominio necesita)

### 2. **Capa de Aplicación** (`aplicacion/`)
**Responsabilidad**: Orquestar la lógica de negocio y exponer servicios.

- **`servicios/`**: Implementaciones de los puertos de entrada
  - Coordinan operaciones
  - Aplican lógica transversal (auditoría, eventos, etc.)
  - Inyectan adaptadores de persistencia

- **`controladores/`**: Adaptadores REST HTTP
  - Reciben peticiones HTTP
  - Traducen DTO → Modelos de Dominio
  - Llaman a servicios de aplicación
  - Devuelven respuestas HTTP

### 3. **Capa de Infraestructura** (`infraestructura/`)
**Responsabilidad**: Detalles técnicos y adaptadores externos.

- **`seguridad/`**: Configuración de seguridad
  - `ConfiguracionSeguridad`: Configuración de Spring Security
  - `FiltroAutenticacionFirebase`: Validación de JWT
  - `ConfiguracionFirebase`: Inicialización del SDK

- **`persistencia/`**: Adaptadores de base de datos
  - `mongo/`: Interfaces Spring Data MongoDB
  - `adaptadores/`: Implementaciones de los puertos de salida

## Flujo de Datos

### Crear una Transacción

```
POST /api/transacciones
{
  "cuentaId": "123",
  "monto": 50.00,
  "tipo": "GASTO",
  "descripcion": "Compra"
}
↓
ControladorTransaccion.crearTransaccion()
↓
ServicioGestionTransaccion.crearTransaccion()
↓
AdaptadorPersistenciaTransaccion.guardarTransaccion()
↓
Transaccion guardada + ResumenMensual actualizado (UPSERT atómico)
↓
{
  "id": "xyz",
  "estado": "CREADA",
  "timestamp": "2025-10-16T12:00:00"
}
```

## Ventajas de esta Arquitectura

✅ **Desacoplamiento**: El dominio no conoce sobre MongoDB, Spring, HTTP
✅ **Testeable**: Fácil hacer tests unitarios sin infraestructura
✅ **Flexible**: Cambiar MongoDB por otra BD sin afectar el dominio
✅ **Escalable**: Agregar nuevos adaptadores sin modificar código existente
✅ **Mantenible**: Código organizado por responsabilidad

## Patrones Aplicados

1. **Hexagonal (Puertos y Adaptadores)**
   - Aísla la lógica de negocio de detalles técnicos

2. **Inyección de Dependencias**
   - Spring gestiona las dependencias automáticamente

3. **Repository Pattern**
   - Abstrae la persistencia en interfaces

4. **DTO (Data Transfer Objects)**
   - Separación entre modelos de datos y API REST (próximo)

## Extensión Futura

Cuando agreguemos funcionalidades:

```
controllers/ (REST)
    ↓
servicios/ (Aplicación)
    ↓
puertos/ (Dominio)
    ↓
adaptadores/ (Infraestructura)
```

Cada nuevo caso de uso sigue el mismo patrón hexagonal.
