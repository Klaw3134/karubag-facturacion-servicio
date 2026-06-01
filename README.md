# facturacion-servicio

Microservicio de gestión de facturación para la plataforma Karübag.

## Descripción
Gestiona la facturación de los servicios de reciclaje para clientes residenciales y corporativos. Se comunica con retiro-servicio para validar los retiros asociados a cada factura.

## Tecnologías
- Java 21
- Spring Boot 3.5.14
- Spring Data JPA
- PostgreSQL (Neon)
- WebClient (Spring WebFlux)

## Puerto
`8090`

## Base de datos
`karubag_facturacion`

## Comunicación con otros servicios
- `retiro-servicio` (:8086) — verifica que el retiro existe

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/facturas | Listar todas las facturas |
| GET | /api/facturas/cliente/{clienteId} | Listar por cliente |
| GET | /api/facturas/estado/{estado} | Listar por estado |
| GET | /api/facturas/periodo | Listar por periodo |
| GET | /api/facturas/cliente/{clienteId}/total-pagado | Total pagado por cliente |
| GET | /api/facturas/{id} | Obtener factura por ID |
| POST | /api/facturas | Crear factura |
| PUT | /api/facturas/{id} | Actualizar factura |
| PUT | /api/facturas/{id}/pagar | Marcar como pagada |
| PUT | /api/facturas/{id}/anular | Anular factura |
| DELETE | /api/facturas/{id} | Eliminar factura |

## Estados de factura
`PENDIENTE`, `PAGADA`, `ANULADA`

## Cómo ejecutar
```bash
./mvnw spring-boot:run
```

## Variables de entorno
```
spring.datasource.url=jdbc:postgresql://<host>/karubag_facturacion
spring.datasource.username=<usuario>
spring.datasource.password=<contraseña>
```