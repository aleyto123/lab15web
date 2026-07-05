# Laboratorio 15 - Observabilidad con Actuator, Prometheus y Grafana

Alumno: Rony Coello

Curso: Desarrollo de aplicaciones Web

Carpeta de entrega: `laboratorio15-observabilidad`

## 1. Objetivo

Implementar observabilidad en microservicios Spring Boot usando:

- Spring Boot Actuator.
- Micrometer Prometheus.
- Prometheus para recoleccion de metricas.
- Grafana para visualizacion.
- Eureka para descubrimiento de servicios.
- API Gateway para consumir los servicios desde una entrada unica.

Servicios solicitados por la guia:

- `cliente-service`
- `categoria-service`
- `pedido-service`

Tambien se agregan:

- `config-server`
- `eureka-server`
- `gateway-service`

## 2. Puertos

| Servicio | Puerto | URL base |
| --- | ---: | --- |
| Config Server | 8888 | `http://localhost:8888` |
| Eureka Server | 8761 | `http://localhost:8761` |
| Gateway Service | 8090 | `http://localhost:8090` |
| Cliente Service | 8081 | `http://localhost:8081` |
| Categoria Service | 8082 | `http://localhost:8082` |
| Pedido Service | 8083 | `http://localhost:8083` |
| Prometheus | 9090 | `http://localhost:9090` |
| Grafana | 3000 | `http://localhost:3000` |

## 3. Archivos importantes

| Ruta | Uso |
| --- | --- |
| `config-repo/` | Configuracion centralizada de los servicios. |
| `prometheus/prometheus.yml` | Configuracion de Prometheus para Docker. |
| `prometheus/prometheus-local.yml` | Configuracion de Prometheus si se ejecuta instalado en Windows. |
| `grafana/provisioning/` | Provisiona datasource y dashboard en Grafana con Docker. |
| `postman/Laboratorio15-Observabilidad.postman_collection.json` | Coleccion Postman completa. |
| `evidencias/` | Carpeta para guardar capturas. |
| `docker-compose.yml` | Levanta servicios, Prometheus y Grafana. |

## 4. Dependencias agregadas en cada servicio

En `cliente-service`, `categoria-service` y `pedido-service` se agrego Actuator:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Tambien se agrego Prometheus:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

## 5. Configuracion Actuator y Prometheus

En `config-repo/cliente-service.yml`, `config-repo/categoria-service.yml` y `config-repo/pedido-service.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
  prometheus:
    metrics:
      export:
        enabled: true
```

Esto habilita:

- `/actuator`
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

## 6. Compilar el proyecto

Desde esta carpeta:

```bash
mvn clean package -DskipTests
```

Evidencia esperada:

```text
BUILD SUCCESS
```

Importante: este comando se ejecuta desde la raiz del proyecto, o sea:

```text
C:\Users\ronyc\Documents\examen de empresariales\laboratorio15-observabilidad
```

No ejecutes `mvn spring-boot:run` dentro de `prometheus` ni dentro de `grafana`. Esas carpetas no son aplicaciones Spring Boot, solo contienen archivos de configuracion para esas herramientas.

## 7. Ejecutar con Docker Compose

Primero compilar:

```bash
mvn clean package -DskipTests
```

Luego levantar todo:

```bash
docker compose up --build
```

Se levantan:

- Config Server.
- Eureka Server.
- Gateway Service.
- Cliente Service.
- Categoria Service.
- Pedido Service.
- Prometheus.
- Grafana.

Para detener:

```bash
docker compose down
```

## 8. Ejecutar sin Docker

Abrir terminales separadas y ejecutar en orden.

Forma facil con scripts desde la raiz `laboratorio15-observabilidad`:

```powershell
.\scripts\start-config-server.ps1
.\scripts\start-eureka-server.ps1
.\scripts\start-cliente-service.ps1
.\scripts\start-categoria-service.ps1
.\scripts\start-pedido-service.ps1
.\scripts\start-gateway-service.ps1
```

Cada comando debe ir en una terminal separada, porque cada servicio queda ejecutandose.

Forma manual:

Terminal 1:

```bash
cd config-server
mvn spring-boot:run
```

Terminal 2:

```bash
cd eureka-server
mvn spring-boot:run
```

Terminal 3:

```bash
cd cliente-service
mvn spring-boot:run
```

Terminal 4:

```bash
cd categoria-service
mvn spring-boot:run
```

Terminal 5:

```bash
cd pedido-service
mvn spring-boot:run
```

Terminal 6:

```bash
cd gateway-service
mvn spring-boot:run
```

## 9. Como probar en Postman sin confundirse

En Postman siempre haces lo mismo:

1. Abre Postman.
2. Crea una nueva request.
3. Escoge el metodo: `GET`, `POST`, `PUT` o `DELETE`.
4. Pega la URL completa que aparece en esta guia.
5. Si es `POST` o `PUT`, entra a `Body`, selecciona `raw`, selecciona `JSON` y pega el JSON que se indica.
6. Presiona `Send`.
7. Toma captura mostrando la URL, el metodo, el body si existe y la respuesta.

Nota: la coleccion Postman ya esta creada en:

```text
postman/Laboratorio15-Observabilidad.postman_collection.json
```

Pero abajo tambien estan todas las pruebas con URLs completas para que no tengas que adivinar nada.

## 10. Primero verifica que los servicios esten prendidos

Antes de probar, deben estar corriendo estos servicios:

| Servicio | URL para revisar | Que debe pasar |
| --- | --- | --- |
| Config Server | `http://localhost:8888/actuator/health` | Sale `status: UP` |
| Eureka Server | `http://localhost:8761` | Abre una pagina web |
| Cliente Service | `http://localhost:8081/actuator/health` | Sale `status: UP` |
| Categoria Service | `http://localhost:8082/actuator/health` | Sale `status: UP` |
| Pedido Service | `http://localhost:8083/actuator/health` | Sale `status: UP` |
| Gateway Service | `http://localhost:8090/actuator/health` | Sale `status: UP` |

Si uno no responde, ese servicio no esta levantado todavia.

## 11. Pruebas de Config Server

Aqui se revisa que Config Server esta entregando la configuracion de cada microservicio.

### 11.1 Probar configuracion de cliente-service

En Postman:

```text
Metodo: GET
URL: http://localhost:8888/cliente-service/default
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

En la respuesta debe verse algo parecido a esto:

```json
{
  "name": "cliente-service",
  "profiles": ["default"],
  "propertySources": [
    {
      "source": {
        "server.port": 8081,
        "management.endpoints.web.exposure.include": "*",
        "management.endpoint.health.show-details": "always",
        "management.prometheus.metrics.export.enabled": true
      }
    }
  ]
}
```

No tiene que salir identico, pero si deben aparecer esas propiedades.

### 11.2 Probar configuracion de categoria-service

En Postman:

```text
Metodo: GET
URL: http://localhost:8888/categoria-service/default
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

Debe verse `server.port` con valor `8082`.

### 11.3 Probar configuracion de pedido-service

En Postman:

```text
Metodo: GET
URL: http://localhost:8888/pedido-service/default
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

Debe verse `server.port` con valor `8083`.

## 12. Pruebas de Eureka

Esto no se prueba con JSON. Solo abre el navegador y pega:

```text
http://localhost:8761
```

Resultado esperado:

En la pagina de Eureka deben aparecer estos nombres:

```text
CLIENTE-SERVICE
CATEGORIA-SERVICE
PEDIDO-SERVICE
GATEWAY-SERVICE
```

Si no aparecen al toque, espera 20 o 30 segundos y actualiza la pagina.

## 13. Pruebas de Actuator

Actuator sirve para ver si el microservicio esta vivo y que metricas expone.

### 13.1 Cliente Service

Prueba 1:

```text
Metodo: GET
URL: http://localhost:8081/actuator
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

Debe salir una lista de enlaces como `health`, `metrics` y `prometheus`.

Prueba 2:

```text
Metodo: GET
URL: http://localhost:8081/actuator/health
Body: no lleva body
```

Resultado esperado:

```json
{
  "status": "UP"
}
```

Prueba 3:

```text
Metodo: GET
URL: http://localhost:8081/actuator/metrics
Body: no lleva body
```

Resultado esperado: debe salir una lista de metricas, por ejemplo `jvm.memory.used`, `process.cpu.usage` o `http.server.requests`.

### 13.2 Categoria Service

```text
Metodo: GET
URL: http://localhost:8082/actuator
Body: no lleva body
```

```text
Metodo: GET
URL: http://localhost:8082/actuator/health
Body: no lleva body
```

```text
Metodo: GET
URL: http://localhost:8082/actuator/metrics
Body: no lleva body
```

Resultado esperado:

- Las tres pruebas deben responder `Status: 200 OK`.
- En health debe salir `status: UP`.
- En metrics debe salir una lista de nombres de metricas.

### 13.3 Pedido Service

```text
Metodo: GET
URL: http://localhost:8083/actuator
Body: no lleva body
```

```text
Metodo: GET
URL: http://localhost:8083/actuator/health
Body: no lleva body
```

```text
Metodo: GET
URL: http://localhost:8083/actuator/metrics
Body: no lleva body
```

Resultado esperado:

- Las tres pruebas deben responder `Status: 200 OK`.
- En health debe salir `status: UP`.
- En metrics debe salir una lista de nombres de metricas.

## 14. Pruebas de Prometheus en cada servicio

Estas URLs muestran las metricas en el formato que Prometheus puede leer.

### 14.1 Cliente Service

```text
Metodo: GET
URL: http://localhost:8081/actuator/prometheus
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

La respuesta no es JSON. Es texto plano. Deben salir lineas como:

```text
# HELP application_started_time_seconds
jvm_memory_used_bytes
http_server_requests_seconds_count
```

### 14.2 Categoria Service

```text
Metodo: GET
URL: http://localhost:8082/actuator/prometheus
Body: no lleva body
```

Resultado esperado: `Status: 200 OK` y texto plano con metricas.

### 14.3 Pedido Service

```text
Metodo: GET
URL: http://localhost:8083/actuator/prometheus
Body: no lleva body
```

Resultado esperado: `Status: 200 OK` y texto plano con metricas.

## 15. Pruebas normales por Gateway

El Gateway corre en el puerto `8090`. Estas pruebas sirven para generar trafico y despues verlo en las metricas.

### 15.1 Listar clientes

```text
Metodo: GET
URL: http://localhost:8090/api/clientes
Body: no lleva body
```

Resultado esperado:

```text
Status: 200 OK
```

Respuesta ejemplo:

```json
[
  {
    "id": 1,
    "nombre": "Ana Torres",
    "correo": "ana.torres@demo.com",
    "telefono": "987111222"
  },
  {
    "id": 2,
    "nombre": "Luis Ramirez",
    "correo": "luis.ramirez@demo.com",
    "telefono": "987333444"
  }
]
```

### 15.2 Crear cliente

En Postman:

```text
Metodo: POST
URL: http://localhost:8090/api/clientes
Body: raw / JSON
```

Pega este body:

```json
{
  "nombre": "Rosa Medina",
  "correo": "rosa.medina@demo.com",
  "telefono": "987555666"
}
```

Resultado esperado:

```text
Status: 201 Created
```

Respuesta ejemplo:

```json
{
  "id": 3,
  "nombre": "Rosa Medina",
  "correo": "rosa.medina@demo.com",
  "telefono": "987555666"
}
```

### 15.3 Listar categorias

```text
Metodo: GET
URL: http://localhost:8090/api/categorias
Body: no lleva body
```

Resultado esperado: `Status: 200 OK`.

### 15.4 Crear categoria

```text
Metodo: POST
URL: http://localhost:8090/api/categorias
Body: raw / JSON
```

Pega este body:

```json
{
  "nombre": "Hogar",
  "descripcion": "Productos para el hogar"
}
```

Resultado esperado:

```text
Status: 201 Created
```

### 15.5 Listar pedidos

```text
Metodo: GET
URL: http://localhost:8090/api/pedidos
Body: no lleva body
```

Resultado esperado: `Status: 200 OK`.

### 15.6 Crear pedido

```text
Metodo: POST
URL: http://localhost:8090/api/pedidos
Body: raw / JSON
```

Pega este body:

```json
{
  "clienteId": 1,
  "categoriaId": 1,
  "producto": "Monitor 24 pulgadas",
  "cantidad": 2,
  "total": 1380.00
}
```

Resultado esperado:

```text
Status: 201 Created
```

Respuesta ejemplo:

```json
{
  "id": 3,
  "clienteId": 1,
  "categoriaId": 1,
  "producto": "Monitor 24 pulgadas",
  "cantidad": 2,
  "total": 1380.00
}
```

## 16. Prometheus instalado en Windows

Si usas Prometheus instalado manualmente, copia o usa este archivo:

```text
prometheus/prometheus-local.yml
```

Ejecutar desde la carpeta donde esta `prometheus.exe`:

```bash
prometheus.exe --config.file="C:\Users\ronyc\Documents\examen de empresariales\laboratorio15-observabilidad\prometheus\prometheus-local.yml"
```

Abrir:

```text
http://localhost:9090
```

Probar consulta:

```text
up
```

Abrir targets:

```text
http://localhost:9090/targets
```

Resultado esperado:

- Targets `cliente-service-local`, `categoria-service-local`, `pedido-service-local` y `gateway-service-local` en estado `UP`.

## 16. Prometheus con Docker Compose

Si se usa `docker compose up --build`, Prometheus ya queda configurado con:

```text
prometheus/prometheus.yml
```

Abrir:

```text
http://localhost:9090/targets
```

Resultado esperado:

- `cliente-service`
- `categoria-service`
- `pedido-service`
- `gateway-service`

Todos deben estar `UP`.

Consulta recomendada:

```text
up
```

Otra consulta util:

```text
sum by (job) (rate(http_server_requests_seconds_count[1m]))
```

## 17. Grafana

Con Docker Compose:

```text
http://localhost:3000/login
```

Credenciales:

```text
Usuario: admin
Contrasena: admin
```

La primera vez puede pedir cambiar la contrasena.

Datasource:

```text
Prometheus -> http://prometheus:9090
```

Dashboard incluido:

```text
Laboratorio 15 - Observabilidad
```

Si Grafana se instala manualmente en Windows, configurar el datasource con:

```text
URL: http://localhost:9090
```

## 18. Evidencias que debes capturar

Guardar en:

```text
evidencias/
```

Orden sugerido:

| Archivo sugerido | Que debe mostrar |
| --- | --- |
| `01-build-success.png` | Terminal con `BUILD SUCCESS`. |
| `02-servicios-iniciados.png` | Servicios corriendo o `docker compose up`. |
| `03-eureka-servicios.png` | Eureka con `CLIENTE-SERVICE`, `CATEGORIA-SERVICE`, `PEDIDO-SERVICE`, `GATEWAY-SERVICE`. |
| `04-actuator-cliente.png` | `GET {{cliente}}/actuator`. |
| `05-actuator-categoria.png` | `GET {{categoria}}/actuator`. |
| `06-actuator-pedido.png` | `GET {{pedido}}/actuator`. |
| `07-health-pedido.png` | `GET {{pedido}}/actuator/health` con `UP`. |
| `08-metrics-pedido.png` | `GET {{pedido}}/actuator/metrics`. |
| `09-prometheus-pedido.png` | `GET {{pedido}}/actuator/prometheus`. |
| `10-prometheus-targets.png` | `http://localhost:9090/targets` con targets `UP`. |
| `11-grafana-datasource.png` | Grafana con datasource Prometheus. |
| `12-grafana-dashboard.png` | Dashboard con graficos. |
| `13-postman-cliente-post.png` | POST de cliente con body y respuesta 201. |
| `14-postman-categoria-post.png` | POST de categoria con body y respuesta 201. |
| `15-postman-pedido-post.png` | POST de pedido con body y respuesta 201. |

## 19. Conclusiones

1. Actuator permite revisar el estado interno de cada microservicio mediante endpoints como `/actuator/health` y `/actuator/metrics`.
2. Micrometer expone las metricas en formato compatible con Prometheus mediante `/actuator/prometheus`.
3. Prometheus centraliza la recoleccion de metricas y permite verificar si los servicios estan disponibles con la consulta `up`.
4. Grafana facilita visualizar las metricas recolectadas por Prometheus en paneles faciles de interpretar.
5. La observabilidad permite detectar problemas de disponibilidad, consumo de recursos y trafico HTTP sin revisar manualmente los logs de cada servicio.
