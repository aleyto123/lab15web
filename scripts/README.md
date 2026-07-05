# Scripts de inicio

Ejecuta cada script en una terminal separada y en este orden:

```powershell
.\scripts\start-config-server.ps1
.\scripts\start-eureka-server.ps1
.\scripts\start-cliente-service.ps1
.\scripts\start-categoria-service.ps1
.\scripts\start-pedido-service.ps1
.\scripts\start-gateway-service.ps1
```

No ejecutes `mvn spring-boot:run` dentro de `prometheus` ni `grafana`, porque esas carpetas solo tienen archivos de configuracion.
