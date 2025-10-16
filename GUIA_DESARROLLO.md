# Guía de Desarrollo - Arquitectura Hexagonal

## Cómo Agregar una Nueva Funcionalidad

### Ejemplo: Crear una Transacción

#### 1. Definir Puerto de Entrada (Caso de Uso) en Dominio

```java
// dominio/puertos/entrada/ServicioGestionTransaccion.java
public interface ServicioGestionTransaccion {
    Transaccion crearTransaccion(Transaccion transaccion);
    // ... otros métodos
}
```

#### 2. Implementar el Servicio de Aplicación

```java
// aplicacion/servicios/ServicioGestionTransaccionImpl.java
@Service
public class ServicioGestionTransaccionImpl implements ServicioGestionTransaccion {
    
    private final RepositorioTransaccion repositorioTransaccion;
    private final RepositorioResumenMensual repositorioResumenMensual;
    
    public ServicioGestionTransaccionImpl(
            RepositorioTransaccion repositorioTransaccion,
            RepositorioResumenMensual repositorioResumenMensual) {
        this.repositorioTransaccion = repositorioTransaccion;
        this.repositorioResumenMensual = repositorioResumenMensual;
    }
    
    @Override
    public Transaccion crearTransaccion(Transaccion transaccion) {
        // Validar reglas de negocio
        validarTransaccion(transaccion);
        
        // Guardar transacción
        Transaccion transaccionGuardada = repositorioTransaccion.guardarTransaccion(transaccion);
        
        // Actualizar resumen mensual
        actualizarResumenMensual(transaccionGuardada);
        
        return transaccionGuardada;
    }
    
    private void validarTransaccion(Transaccion transaccion) {
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (transaccion.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }
    }
    
    private void actualizarResumenMensual(Transaccion transaccion) {
        // Lógica de negocio: actualizar resumen
        LocalDateTime fecha = transaccion.getFechaTransaccion();
        Integer anio = fecha.getYear();
        Integer mes = fecha.getMonthValue();
        
        Optional<ResumenMensual> resumenOpt = repositorioResumenMensual
            .obtenerResumenPorCuentaYPeriodo(
                transaccion.getCuentaId(),
                anio,
                mes
            );
        
        ResumenMensual resumen = resumenOpt.orElseGet(() -> 
            crearResumenMensual(transaccion, anio, mes)
        );
        
        // Actualizar totales según tipo de transacción
        if ("INGRESO".equals(transaccion.getTipo())) {
            resumen.setTotalIngresos(
                resumen.getTotalIngresos().add(transaccion.getMonto())
            );
        } else if ("GASTO".equals(transaccion.getTipo())) {
            resumen.setTotalEgresos(
                resumen.getTotalEgresos().add(transaccion.getMonto())
            );
        }
        
        repositorioResumenMensual.actualizar(resumen);
    }
    
    private ResumenMensual crearResumenMensual(Transaccion transaccion, Integer anio, Integer mes) {
        return ResumenMensual.builder()
            .usuarioId(transaccion.getUsuarioId())
            .cuentaId(transaccion.getCuentaId())
            .anio(anio)
            .mes(mes)
            .saldoInicial(BigDecimal.ZERO)
            .saldoFinal(BigDecimal.ZERO)
            .totalIngresos(BigDecimal.ZERO)
            .totalEgresos(BigDecimal.ZERO)
            .fechaCreacion(LocalDateTime.now())
            .build();
    }
}
```

#### 3. Crear DTO para la Petición HTTP

```java
// aplicacion/dtos/CrearTransaccionDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearTransaccionDTO {
    
    @NotBlank(message = "El ID de la cuenta es requerido")
    private String cuentaId;
    
    @NotBlank(message = "El tipo de transacción es requerido")
    private String tipo; // INGRESO, GASTO
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;
    
    private String descripcion;
    
    private String categoriaId;
    
    private LocalDateTime fechaTransaccion;
    
    private List<String> etiquetas;
}
```

#### 4. Crear Controlador REST

```java
// aplicacion/controladores/ControladorTransaccion.java
@RestController
@RequestMapping("/api/transacciones")
public class ControladorTransaccion {
    
    private final ServicioGestionTransaccion servicioGestionTransaccion;
    
    public ControladorTransaccion(ServicioGestionTransaccion servicioGestionTransaccion) {
        this.servicioGestionTransaccion = servicioGestionTransaccion;
    }
    
    @PostMapping
    public ResponseEntity<Transaccion> crear(
            @Valid @RequestBody CrearTransaccionDTO dto,
            @AuthenticationPrincipal String usuarioId) {
        
        Transaccion transaccion = Transaccion.builder()
            .usuarioId(usuarioId)
            .cuentaId(dto.getCuentaId())
            .categoriaId(dto.getCategoriaId())
            .tipo(dto.getTipo())
            .monto(dto.getMonto())
            .descripcion(dto.getDescripcion())
            .etiquetas(dto.getEtiquetas())
            .fechaTransaccion(dto.getFechaTransaccion() != null ? dto.getFechaTransaccion() : LocalDateTime.now())
            .fechaCreacion(LocalDateTime.now())
            .build();
        
        Transaccion transaccionGuardada = servicioGestionTransaccion.crearTransaccion(transaccion);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(transaccionGuardada);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> obtenerPorId(@PathVariable String id) {
        Optional<Transaccion> transaccion = servicioGestionTransaccion.obtenerTransaccion(id);
        return transaccion
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Transaccion>> listarDelUsuario(
            @AuthenticationPrincipal String usuarioId) {
        List<Transaccion> transacciones = servicioGestionTransaccion.listarTransaccionesUsuario(usuarioId);
        return ResponseEntity.ok(transacciones);
    }
}
```

## Flujo Completo

```
1. Cliente envia:
   POST /api/transacciones
   {
     "cuentaId": "123",
     "tipo": "GASTO",
     "monto": 50.00,
     "descripcion": "Compra"
   }

2. FiltroAutenticacionFirebase
   - Valida JWT
   - Extrae UID

3. ControladorTransaccion.crear()
   - Valida DTO (@Valid)
   - Crea objeto Transaccion
   - Llama servicio

4. ServicioGestionTransaccionImpl.crearTransaccion()
   - Valida reglas de negocio
   - Llama repositorio
   - Actualiza resumen

5. AdaptadorPersistenciaTransaccion.guardarTransaccion()
   - Guarda en MongoDB

6. Respuesta HTTP 201 Created
   {
     "id": "xyz123",
     "usuarioId": "uid",
     "cuentaId": "123",
     "monto": 50.00,
     ...
   }
```

## Reglas a Seguir

✅ **DO's**
- Inyectar dependencias en constructor
- Usar `@Service` en servicios de aplicación
- Usar `@Component` en adaptadores
- Usar `@RestController` en controladores
- Validar en el servicio de aplicación
- Traducir DTO → Modelo de dominio en el controlador
- Usar Optional en lugar de null
- Registrar logs relevantes
- Documentar métodos complejos

❌ **DON'Ts**
- No usar `new` para crear beans
- No tener lógica de negocio en controladores
- No mezclar DTOs con modelos de dominio
- No ignorar validaciones
- No hacer queries directas sin pasar por repositorio
- No importar Firebase en capas superiores
- No usar "new Exception" sin contexto
- No retornar null sin Optional

## Testing

```java
// test/java/com/proyecto/aplicacion/servicios/ServicioGestionTransaccionImplTest.java
@ExtendWith(MockitoExtension.class)
public class ServicioGestionTransaccionImplTest {
    
    @Mock
    private RepositorioTransaccion repositorioTransaccion;
    
    @Mock
    private RepositorioResumenMensual repositorioResumenMensual;
    
    @InjectMocks
    private ServicioGestionTransaccionImpl servicio;
    
    @Test
    void debeCrearTransaccionValida() {
        // Arrange
        Transaccion transaccion = Transaccion.builder()
            .usuarioId("user123")
            .cuentaId("cuenta456")
            .monto(BigDecimal.valueOf(100))
            .tipo("GASTO")
            .build();
        
        when(repositorioTransaccion.guardarTransaccion(any()))
            .thenReturn(transaccion);
        
        // Act
        Transaccion resultado = servicio.crearTransaccion(transaccion);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("GASTO", resultado.getTipo());
        verify(repositorioTransaccion).guardarTransaccion(transaccion);
    }
    
    @Test
    void debeRechazarTransaccionConMontoNegativo() {
        // Arrange
        Transaccion transaccion = Transaccion.builder()
            .monto(BigDecimal.valueOf(-100))
            .build();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> servicio.crearTransaccion(transaccion));
    }
}
```

## Resumen

1. **Puertos** (Interfaces en Dominio) → Definen qué se puede hacer
2. **Servicios** (Aplicación) → Implementan la lógica
3. **Adaptadores** (Infraestructura) → Concretan la tecnología
4. **Controladores** (Aplicación) → Exponen HTTP
5. **DTOs** (Aplicación) → Validan entrada

Todo desacoplado y testeable. ¡Así hacemos código profesional!
