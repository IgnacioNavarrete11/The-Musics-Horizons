# The Musics Horizons

<p align="center">
  <img src="app/src/main/res/drawable/the_musics_horizons_logo.png" alt="App Logo" width="200">
</p>

### Tu universo musical en una sola aplicación.

**The Musics Horizons** es una aplicación Android diseñada para ser el compañero definitivo de todo amante de la música. Permite a los usuarios descubrir nueva música, encontrar conciertos y eventos cercanos, y llevar un registro de su propio universo musical. La aplicación cuenta con un robusto sistema de perfiles y un panel de administración con funcionalidades avanzadas.

---

## 🎯 Público Objetivo

Esta aplicación está dirigida a:

- **Amantes de la música** que asisten a conciertos y festivales con frecuencia.
- **Usuarios curiosos** que disfrutan descubriendo nueva música en cualquier lugar.
- **Desarrolladores Android** que buscan un proyecto de ejemplo con una arquitectura limpia y funcionalidades del mundo real.

---

## ✨ Características Principales

### Para el Usuario:

- **🎵 Reconocimiento de Canciones (Scan Audio):**
  - Utiliza el micrófono del dispositivo para identificar una canción que esté sonando en el ambiente.
  - Muestra información detallada del tema reconocido: artista, título, álbum y carátula.
  - Ofrece la opción de guardar la canción en el "horizonte" personal del usuario.

- **🎟️ Explorador de Eventos (Explore Events):**
  - Busca automáticamente conciertos y eventos musicales cercanos usando la ubicación GPS del usuario.
  - Permite realizar búsquedas manuales de eventos por palabra clave (ciudad, artista, etc.).
  - Guarda eventos de interés para consultarlos más tarde.

- **👁️ Escáner Visual (Scan Visual):**
  - Escanea códigos QR para abrir enlaces o mostrar información de forma rápida.

- **👤 Perfil de Usuario Personalizado:**
  - **Autenticación Segura:** Sistema de registro y login basado en email y contraseña.
  - **Foto de Perfil:** El usuario puede elegir una foto de su galería, que se muestra en su perfil y en la lista de usuarios para el administrador.
  - **Autogestión de Cuenta:**
    - **Cambiar Nombre:** Opción para modificar el nombre de usuario.
    - **Quitar Foto:** Restablece la imagen de perfil a la predeterminada.
    - **Borrar Cuenta:** Opción segura (con diálogo de confirmación) para eliminar permanentemente la cuenta y todos los datos asociados (canciones guardadas, eventos, etc.).

### Para el Administrador:

- **👑 Panel de Administración:**
  - Un botón de **"Gestión de Usuarios"** aparece exclusivamente en el perfil del administrador.
  - **Creación de Admin:** El primer usuario que se registre con el email `admin@gmail.com` obtiene privilegios de administrador.

- **📊 Gestión y Estadísticas de Usuarios:**
  - **Lista Completa de Usuarios:** Muestra a todos los usuarios registrados con su foto de perfil, nombre y email.
  - **Estadísticas de Uso:** Al seleccionar a un usuario, el administrador puede ver un resumen de su actividad:
    - Número de búsquedas de audio.
    - Número de escaneos visuales.
    - Número de búsquedas de eventos.
  - **Poderes de Moderación (con diálogos de confirmación):**
    - Cambiar el nombre de cualquier usuario.
    - Quitar la foto de perfil de cualquier usuario.
    - Eliminar permanentemente la cuenta de cualquier usuario.

---

## 🛠️ Entorno de Desarrollo

- **IDE:** Android Studio Hedgehog | 2023.1.1
- **Lenguaje:** Java
- **Base de Datos:** SQLite
- **Componentes Jetpack:** CameraX, ViewBinding, Navigation
- **Otras Librerías:** Glide, Google ML Kit, Google Play Services, Retrofit

---

## 📊 Diagramas de la Aplicación

A continuación, se presentan los diagramas que ilustran la arquitectura, flujo y diseño de la aplicación.
<details>
<summary>Ver todos los diagramas de diseño</summary>
  
### 1. Diagrama de Diseño (Flujo de Pantallas)

![Diagrama de Diseño (Técnico)] <img width="1041" height="867" alt="Diseño tecnico" src="https://github.com/user-attachments/assets/34fa4f79-9a22-48f1-b7db-f38ba5f4b1f4" />

![Diagrama de Diseño (Básico)] <img width="3183" height="221" alt="Diseño basico" src="https://github.com/user-attachments/assets/60923a59-b452-420c-b277-ddccf14436c9" />


### 2. Diagrama de Clases

![Diagrama de Clases (Técnico)] <img width="1162" height="385" alt="Clases tecnico" src="https://github.com/user-attachments/assets/881523d6-b542-4da9-9410-909ea1529ade" />

![Diagrama de Clases (Básico)] <img width="593" height="353" alt="Clases basico" src="https://github.com/user-attachments/assets/38d73c98-a2b7-4507-aae1-077ae474a5dc" />


### 3. Diagramas de Flujo (Lógica Principal)

<details>
<summary>Ver todos los diagramas de flujo</summary>

#### Flujo de Autenticación
- **Login:**
  ![Flujo de Login (Técnico)] <img width="625" height="615" alt="Inicio tecnico" src="https://github.com/user-attachments/assets/c2197187-580a-40c6-a26e-5927f9836cd2" />

  ![Flujo de Login (Básico)] <img width="353" height="453" alt="inicio basico" src="https://github.com/user-attachments/assets/971ec0d9-a56c-44cc-868b-4698e0106e3e" />


#### Flujo de Funcionalidades
- **Búsqueda de Eventos:**
  ![Flujo de Búsqueda de Eventos (Técnico)] <img width="2426" height="230" alt="evento tecnico" src="https://github.com/user-attachments/assets/3521bfcb-22c2-44f5-9043-1e311272c783" />

  ![Flujo de Búsqueda de Eventos (Básico)] <img width="1719" height="219" alt="evento basico" src="https://github.com/user-attachments/assets/7ccf2e04-9b58-42d4-aee6-11d393c3b8de" />

- **Reconocimiento de Canción:**
  ![Flujo de Reconocimiento de Canción (Técnico)] <img width="909" height="1228" alt="audio tecnico" src="https://github.com/user-attachments/assets/a8a64f7f-2f00-4bbc-b43e-7ffe6500e07e" />

  ![Flujo de Reconocimiento de Canción (Básico)] <img width="1719" height="219" alt="evento basico" src="https://github.com/user-attachments/assets/8702ed50-1cf9-4f90-8b9f-b0d0a5d017e0" />

</details>

### 4. Diagramas de Secuencia (Interacciones)

<details>
<summary>Ver todos los diagramas de secuencia</summary>

#### Secuencia de Inicio
- **Visión Técnica y Básica:**
  ![Secuencia de Inicio (Técnico)] <img width="942" height="607" alt="Login tecnico" src="https://github.com/user-attachments/assets/95ca9cae-b4b1-4a57-9e68-de5c372e02dd" />
 
  ![Secuencia de Inicio (Básico)]  <img width="674" height="565" alt="Login basico" src="https://github.com/user-attachments/assets/40513a4f-74dc-445d-8674-19d393d660ed" />


#### Secuencia de Reconocimiento de Audio
- **Visión Técnica y Básica:**
  ![Secuencia de Audio (Técnico)]  <img width="1208" height="580" alt="Reconocimiento de audio tecnico" src="https://github.com/user-attachments/assets/62496570-e524-4454-a73e-f359a4e1d5d5" />

  ![Secuencia de Audio (Básico)]  <img width="877" height="535" alt="cancion basico" src="https://github.com/user-attachments/assets/f3052937-38cc-4ad9-b082-0b3a6f6d3905" />


#### Secuencia de Búsqueda de Eventos
- **Visión Técnica y Básica:**
  ![Secuencia de Eventos (Técnico)] <img width="1018" height="551" alt="evento tecnico" src="https://github.com/user-attachments/assets/f3df1d69-51a3-45a6-9026-3867157d5b10" />

  ![Secuencia de Eventos (Básico)] <img width="679" height="493" alt="evento basico" src="https://github.com/user-attachments/assets/6dc7bdec-58b0-4b3b-8812-2b527e4d72be" />


#### Secuencia de Gestión de Cuenta
- **Actualizar Perfil:**
  ![Secuencia de Actualizar Perfil (Técnico)]  <img width="869" height="636" alt="actualizar perfil tecnico" src="https://github.com/user-attachments/assets/aa424a21-cf96-4c40-8970-897af789d95b" />
  ![Secuencia de Actualizar Perfil (Básico)]
  
- **Eliminar Usuario:**
  ![Secuencia de Eliminar Usuario (Técnico)] <img width="1220" height="905" alt="eliminar usuario tecnico" src="https://github.com/user-attachments/assets/98e678ef-ddc8-4454-90d3-cb34f15b4787" />

  ![Secuencia de Eliminar Usuario (Básico)] <img width="713" height="577" alt="Borrar usuario basico" src="https://github.com/user-attachments/assets/72ee6860-f6d0-4320-96f8-060c9db72e32" />

  
- **Guardar Evento:**
  ![Secuencia de Guardar Evento (Técnico)] <img width="784" height="578" alt="Guardar evento tecnico" src="https://github.com/user-attachments/assets/a7cf4226-40a3-4020-9d12-d48a99b01c7f" />

  ![Secuencia de Guardar Evento (Básico)] <img width="679" height="493" alt="evento basico" src="https://github.com/user-attachments/assets/eae7cd0b-6140-40ca-97bf-f774e97285f9" />

  
</details>

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura limpia y organizada para facilitar su mantenimiento y escalabilidad. La estructura de paquetes principal es la siguiente:

- **`activities`**: Contiene las pantallas principales de la aplicación (ej: `LoginActivity`, `HomeActivity`).
- **`fragments`**: Contiene "trozos" de interfaz de usuario que son reutilizables.
- **`models`**: Define la estructura de los datos (clases POJO).
- **`network`**: Contiene las interfaces de Retrofit para definir los endpoints de las APIs.
- **`services`**: Orquesta la comunicación con las APIs, ocultando la complejidad.
- **`utils`**: Contiene clases de utilidad reutilizables (`SessionManager`, `CustomToast`).
- **`adapters`**: Conecta los datos con los `RecyclerView`.
- **`database`**: Gestiona la interacción con la base de datos SQLite.

---

## 🚀 Configuración del Proyecto

Para compilar y ejecutar el proyecto, es fundamental configurar las claves de API, ya que están protegidas y no se incluyen en el control de versiones.

1.  **Clonar el Repositorio:**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```

2.  **Configurar las Claves de API:**
    - En la raíz de tu proyecto Android, busca o crea el archivo `local.properties`.
    - Añade las siguientes líneas, reemplazando `"TU_CLAVE_AQUI"` con tus claves personales:

      ```properties
      TICKETMASTER_API_KEY="TU_CLAVE_DE_TICKETMASTER_AQUI"
      AUDD_API_TOKEN="TU_TOKEN_DE_AUDD_AQUI"
      ```

    - **Importante:** Asegúrate de que el archivo `build.gradle` (Módulo: app) contiene la configuración para leer estas claves.

3.  **Sincronizar y Ejecutar:**
    - Abre el proyecto en Android Studio.
    - Sincroniza Gradle haciendo clic en "Sync Now" si se te solicita.
    - Compila y ejecuta la aplicación en un emulador o dispositivo físico.

---

## 🧠 Lecciones Aprendidas

- **Gestión de Permisos:** La implementación de un `PermissionManager` centralizado simplificó enormemente la solicitud de permisos.
- **Seguridad de las Claves:** Se aprendió la importancia de no incluir claves de API directamente en el código, utilizando `local.properties`.
- **Persistencia de Imágenes:** Se resolvió un complejo problema de caché y permisos al copiar las imágenes de perfil al almacenamiento interno.
- **Interfaz de Usuario Coherente:** La creación de un `CustomToast` demostró la importancia de tener componentes de UI reutilizables.

---

## 📈 Mejoras Futuras

- **Migrar a Kotlin:** Refactorizar el código a Kotlin para un desarrollo más moderno y seguro.
- **Inyección de Dependencias:** Implementar Hilt o Dagger para mejorar la testeabilidad.
- **ViewModel y LiveData:** Adoptar componentes de la arquitectura de Android Jetpack para una UI más robusta.
- **Notificaciones Push:** Añadir notificaciones para alertar sobre eventos guardados.

---

## ✍️ Autor

- **Ignacio Navarrete**

---
