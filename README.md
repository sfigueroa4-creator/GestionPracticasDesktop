# Sistema de Gestión de Prácticas

Aplicación de escritorio en Java para administrar prácticas académicas: grupos, inscripciones, instituciones receptoras y seguimiento de horas.

## Requisitos para ejecutar

- Java 17 o superior → https://www.java.com

El archivo `release/GestionPracticasDesktop.jar` ya incluye todas las dependencias. No se necesita instalar nada más.

## Requisitos para compilar

1. **NetBeans 17+** con soporte para proyectos Ant
2. **Driver OJDBC17** — no se incluye en el repositorio por licencia de Oracle
   - Descarga: https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html
   - Coloca el archivo `ojdbc17.jar` en la carpeta `lib/` del proyecto antes de compilar

## Generar el ejecutable

1. Compilar en NetBeans: `Shift+F11` (Clean and Build)
2. Ejecutar el script de empaquetado:
   - Click derecho en `generar-release.ps1` → **Ejecutar con PowerShell**
3. El resultado queda en `release/GestionPracticasDesktop.jar`

## Configuración de conexión

La aplicación se conecta a Oracle Database. Los parámetros de conexión se guardan en `config.properties` junto al ejecutable y se pueden cambiar desde el botón ⚙ en la pantalla de login.

| Parámetro | Valor por defecto |
|---|---|
| Host | `localhost` |
| Puerto | `1521` |
| Servicio | `XEPDB1` |

## Usuario por defecto

Al crear las tablas por primera vez se genera un usuario administrador:

| Campo | Valor |
|---|---|
| Usuario | `GestionP` |
| Contraseña | `GestionP` |

Cambia la contraseña tras el primer acceso desde el módulo **Usuarios → Cambiar contraseña**.
