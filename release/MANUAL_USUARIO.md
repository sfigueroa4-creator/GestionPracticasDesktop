# Manual de Usuario
# Sistema de Gestión de Prácticas

---

## Tabla de contenidos

1. [Introducción](#1-introducción)
2. [Requisitos del sistema](#2-requisitos-del-sistema)
3. [Primer arranque y configuración](#3-primer-arranque-y-configuración)
4. [Inicio de sesión](#4-inicio-de-sesión)
5. [Roles y permisos](#5-roles-y-permisos)
6. [Módulos del sistema](#6-módulos-del-sistema)
   - 6.1 [Usuarios](#61-usuarios)
   - 6.2 [Prácticas](#62-prácticas)
   - 6.3 [Instituciones](#63-instituciones)
   - 6.4 [Grupos](#64-grupos)
   - 6.5 [Inscripciones](#65-inscripciones)
7. [Configuración de base de datos](#7-configuración-de-base-de-datos)
8. [Preguntas frecuentes](#8-preguntas-frecuentes)

---

## 1. Introducción

El **Sistema de Gestión de Prácticas** es una aplicación de escritorio desarrollada en Java que permite administrar el ciclo completo de las prácticas académicas: desde la creación de prácticas y grupos, hasta la inscripción de estudiantes y el seguimiento de horas cumplidas.

El sistema se conecta a una base de datos Oracle y está diseñado para ser usado por múltiples perfiles dentro de una institución educativa.

---

## 2. Requisitos del sistema

| Componente | Requisito mínimo |
|---|---|
| Sistema operativo | Windows 10 o superior |
| Java | JDK / JRE 17 o superior |
| Base de datos | Oracle Database (XE o superior) |
| Memoria RAM | 512 MB disponibles |
| Espacio en disco | 50 MB |

Para verificar que Java está instalado correctamente, abra una terminal y ejecute:

```
java -version
```

---

## 3. Primer arranque y configuración

### 3.1 Configurar la conexión a la base de datos

Antes de usar el sistema por primera vez es necesario apuntar la aplicación a su servidor Oracle.

1. Inicie la aplicación (`GestionPracticasDesktop.jar`).
2. Inicie sesión con las credenciales de administrador (ver sección 4).
3. En el menú lateral, haga clic en **Configuracion BD**.
4. Introduzca los datos de conexión:
   - **Host**: dirección IP o nombre del servidor Oracle (por defecto `localhost`)
   - **Puerto**: puerto del listener Oracle (por defecto `1521`)
   - **Servicio**: nombre del servicio Oracle (por defecto `XEPDB1`)
5. Haga clic en **Guardar**.

> La configuración se guarda en el archivo `config.properties` junto al ejecutable. Si mueve la aplicación a otro equipo, este archivo debe acompañarla o deberá configurarla de nuevo.

### 3.2 Crear las tablas por primera vez

Si la base de datos está vacía o es una instalación nueva:

1. Acceda a **Configuracion BD** (requiere rol Administrador).
2. Haga clic en **Crear tablas**.
3. El sistema creará todas las tablas necesarias e insertará el usuario administrador por defecto:
   - **Usuario:** `GestionP`
   - **Contraseña:** `GestionP`

> **Importante:** cambie la contraseña del administrador por defecto tras el primer acceso usando la opción **Cambiar contrasena** en el módulo Usuarios.

---

## 4. Inicio de sesión

Al iniciar la aplicación se muestra la pantalla de login.

1. Introduzca su **usuario** (campo Email) y **contraseña**.
2. Haga clic en **Ingresar**.

Si las credenciales son correctas, el sistema abrirá la ventana principal mostrando en la barra de título su nombre y rol activo. El menú lateral mostrará únicamente los módulos a los que su rol tiene acceso.

Si las credenciales son incorrectas, aparecerá el mensaje *"Credenciales inválidas"*. Verifique que el usuario esté activo y que la contraseña sea correcta.

---

## 5. Roles y permisos

El sistema cuenta con seis roles. Cada rol determina qué módulos son visibles y si el usuario puede crear o modificar registros.

| Módulo | ADMIN | DIRECTOR | COORDINADOR | DOCENTE | ESTUDIANTE | INSTITUCION |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| Usuarios | ✏️ | — | — | — | — | — |
| Prácticas | ✏️ | ✏️ | 👁 | — | — | — |
| Instituciones | ✏️ | ✏️ | 👁 | — | — | — |
| Grupos | ✏️ | ✏️ | ✏️ | 👁 | — | — |
| Inscripciones | ✏️ | ✏️ | ✏️ | ✏️ | 👁 | 👁 |
| Configuración BD | ✏️ | — | — | — | — | — |

**✏️** = lectura y escritura &nbsp;&nbsp; **👁** = solo lectura &nbsp;&nbsp; **—** = sin acceso

### Descripción de roles

- **ADMIN** — Acceso total al sistema, incluyendo gestión de usuarios y configuración de base de datos.
- **DIRECTOR** — Gestiona prácticas, instituciones, grupos e inscripciones. No gestiona usuarios ni configuración técnica.
- **COORDINADOR** — Gestiona grupos e inscripciones. Puede consultar prácticas e instituciones pero no modificarlas.
- **DOCENTE** — Puede consultar grupos. Gestiona inscripciones de sus estudiantes.
- **ESTUDIANTE** — Solo puede consultar sus inscripciones.
- **INSTITUCION** — Solo puede consultar las inscripciones asociadas.

---

## 6. Módulos del sistema

### 6.1 Usuarios

> Acceso exclusivo para **ADMIN**.

Permite registrar y gestionar las personas que usarán el sistema.

**Campos:**
- **Nombre** — nombre del usuario
- **Apellido** — apellido del usuario
- **Email** — identificador de acceso al sistema (debe ser único)
- **Rol** — perfil de permisos asignado (ver sección 5)

**Para crear un usuario:**
1. Complete los campos del formulario superior.
2. Seleccione el rol en el desplegable.
3. Haga clic en **Guardar**.

> La contraseña inicial de todos los usuarios creados es `123456`. Comuníquela al usuario y cámbiela cuanto antes usando la opción descrita a continuación.

**Para cambiar la contraseña de un usuario:**
1. Haga clic sobre el usuario en la tabla — sus datos se cargarán en el formulario.
2. Haga clic en **Cambiar contrasena**.
3. Introduzca la nueva contraseña y confírmela en el diálogo que aparece.
4. Haga clic en **OK**.

La tabla inferior muestra todos los usuarios registrados con su nombre, apellido, email y rol.

---

### 6.2 Prácticas

> **ADMIN** y **DIRECTOR** pueden crear y modificar. **COORDINADOR** solo consulta.

Gestiona los tipos de práctica académica disponibles en la institución.

**Campos:**
- **Nombre** — nombre descriptivo de la práctica (ej. *Práctica Empresarial I*)
- **Número** — número identificador de la práctica (ej. `1`, `2`)
- **Horas** — total de horas reglamentarias que debe cumplir el estudiante

**Para crear una práctica:**
1. Complete los tres campos del formulario.
2. Haga clic en **Guardar**.

**Para modificar una práctica:**
1. Haga clic sobre la práctica en la tabla — los datos se cargarán en el formulario.
2. Edite los campos deseados.
3. Haga clic en **Actualizar**.

**Para eliminar una práctica:**
1. Seleccione la práctica en la tabla.
2. Haga clic en **Eliminar**.

> Eliminar una práctica puede fallar si existen grupos asociados a ella. Elimine primero los grupos correspondientes.

---

### 6.3 Instituciones

> **ADMIN** y **DIRECTOR** pueden crear. **COORDINADOR** solo consulta.

Registra las empresas u organizaciones donde los estudiantes realizan sus prácticas.

**Campos:**
- **Nombre** — razón social de la institución
- **NIT** — número de identificación tributaria (debe ser único)
- **Teléfono** — teléfono de contacto

**Para registrar una institución:**
1. Complete los campos del formulario.
2. Haga clic en **Guardar**.

La tabla muestra todas las instituciones registradas.

---

### 6.4 Grupos

> **ADMIN**, **DIRECTOR** y **COORDINADOR** pueden crear. **DOCENTE** solo consulta.

Un grupo agrupa a los estudiantes que realizan una misma práctica en una misma institución bajo la supervisión de un docente.

**Campos:**
- **Nombre grupo** — identificador del grupo (ej. *Grupo A - Práctica I*)
- **Práctica** — práctica académica asociada (desplegable)
- **Docente** — docente asesor responsable del grupo (desplegable)
- **Institución** — empresa donde se realizará la práctica (desplegable)
- **Cupo máximo** — número máximo de estudiantes permitidos

**Para crear un grupo:**
1. Complete el nombre y el cupo.
2. Seleccione la práctica, el docente y la institución en los desplegables.
3. Haga clic en **Guardar**.

> Si los desplegables aparecen vacíos, verifique que existan prácticas, usuarios con rol DOCENTE e instituciones registradas previamente.

---

### 6.5 Inscripciones

> **ADMIN**, **DIRECTOR**, **COORDINADOR** y **DOCENTE** pueden crear. **ESTUDIANTE** e **INSTITUCION** solo consultan.

Registra la asignación de un estudiante a un grupo de práctica y lleva el seguimiento de horas cumplidas.

**Campos:**
- **Estudiante** — estudiante a inscribir (desplegable, muestra usuarios con rol ESTUDIANTE)
- **Grupo** — grupo al que se inscribe (desplegable)
- **Horas** — horas cumplidas al momento del registro (puede ser `0` al inscribir)

**Para inscribir un estudiante:**
1. Seleccione el estudiante y el grupo en los desplegables.
2. Introduzca las horas cumplidas (normalmente `0` al inicio).
3. Haga clic en **Guardar**.

La tabla muestra todas las inscripciones con el nombre del estudiante, el grupo y las horas acumuladas.

---

## 7. Configuración de base de datos

> Acceso exclusivo para **ADMIN**.

Accesible desde el botón **Configuracion BD** en el menú lateral.

### Guardar configuración de conexión

Permite cambiar el servidor Oracle al que se conecta la aplicación. Útil cuando se migra la base de datos a otro servidor.

1. Modifique los campos **Host**, **Puerto** y/o **Servicio**.
2. Haga clic en **Guardar**.
3. Reinicie la aplicación para que los cambios surtan efecto.

### Crear tablas

Crea la estructura completa de la base de datos desde cero. Usar solo en instalaciones nuevas o tras borrar las tablas.

> Si las tablas ya existen, esta operación fallará con un error. Borre las tablas primero si desea reiniciar la base de datos.

### Borrar tablas

**⚠️ Operación destructiva e irreversible.** Elimina todas las tablas y todos los datos del sistema.

El sistema solicitará dos confirmaciones antes de ejecutar la operación. Se recomienda encarecidamente realizar una copia de seguridad de la base de datos Oracle antes de proceder.

---

## 8. Preguntas frecuentes

**¿Por qué los desplegables aparecen vacíos al abrir un módulo?**  
Los desplegables se cargan desde la base de datos al abrir el panel. Si están vacíos, significa que no hay registros del tipo requerido. Por ejemplo, el desplegable de docentes en Grupos solo mostrará usuarios con rol DOCENTE.

**¿Por qué no puedo guardar aunque relleno todos los campos?**  
Su rol no tiene permiso de escritura en ese módulo. Los campos y botones aparecerán deshabilitados. Consulte la tabla de roles en la sección 5.

**Olvidé la contraseña de un usuario. ¿Cómo la recupero?**  
Un administrador puede seleccionar el usuario en el módulo Usuarios y usar **Cambiar contrasena** para asignarle una nueva contraseña.

**La aplicación muestra un error de conexión al iniciar.**  
Verifique que el servidor Oracle esté en ejecución y que los datos de conexión en `config.properties` sean correctos. Acceda a **Configuracion BD** para corregirlos.

**¿Puedo usar el sistema en varios equipos a la vez?**  
Sí, siempre que todos los equipos apunten al mismo servidor Oracle. Cada equipo necesita el archivo `GestionPracticasDesktop.jar` y un `config.properties` con los datos del servidor compartido.

**¿Qué hago si necesito reiniciar completamente la base de datos?**  
Acceda a **Configuracion BD** como administrador, haga clic en **Borrar tablas** y confirme las dos advertencias. Luego haga clic en **Crear tablas** para recrear la estructura con el usuario administrador por defecto (`GestionP` / `GestionP`).

---

*Sistema de Gestión de Prácticas — Documentación de usuario*
