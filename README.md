# 🚗 Parqueadero

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen?style=for-the-badge&logo=springboot)
![Arch Linux](https://img.shields.io/badge/Env-Arch%20Linux-blue?style=for-the-badge&logo=arch-linux)

> **"¿Me conviene ir ahora?"** – Un sistema diseñado para que el usuario tome decisiones informadas basadas en la ocupación real del establecimiento.

---

## 📊 Propósito: Análisis y Decisión
Este sistema no solo registra entradas; transforma los datos en información útil para el cliente final. La interfaz permite observar el flujo vehicular para que el usuario analice:
1. **Disponibilidad Inmediata:** ¿Hay cupos para mi tipo de vehículo (Carro/Moto)?
2. **Optimización de Tiempo:** Evita desplazamientos innecesarios a un establecimiento lleno.
3. **Seguridad de Información:** Datos calculados directamente del servidor para evitar errores de actualización manual.



## ✨ Características para el Usuario
* **Visualización Dinámica:** Panel central que muestra cupos totales vs. cupos ocupados.
* **Filtros por Categoría:** Clasificación precisa para Carros, Motos y vehículos pesados.
* **Transparencia Total:** El estado "Libre" o "Lleno" se calcula en tiempo real mediante lógica de backend, garantizando veracidad.

## 🛠️ Stack Tecnológico

### 🖥️ Backend (El Cerebro)
* **Core:** **Java 21 (LTS)** & **Spring Boot 3.5.8** para un rendimiento robusto y escalable.
* **Seguridad:** Autenticación stateless con **JWT (JSON Web Tokens)**, garantizando sesiones seguras y protegidas.
* **Eficiencia:** Mapeo de datos con **MapStruct** y **Lombok** para respuestas de API ultra-rápidas y código limpio (Boilerplate-free).
* **Persistencia:** **MySQL** con lógica de estados derivados, eliminando errores de actualización manual de cupos.

### 🎨 Frontend (La Experiencia)
* **Framework:** **Next.js 15+** (App Router) para una navegación instantánea y optimización SEO.
* **Lenguaje:** **TypeScript** para un desarrollo tipado, reduciendo errores en tiempo de ejecución.
* **Estilos:** **Tailwind CSS** para una interfaz moderna, responsiva y adaptada a dispositivos móviles.
* **Iconografía:** **Lucide React** para una guía visual clara e intuitiva para el usuario.
* **Gestión de Datos:** **Fetch API** sincronizado con el estado de Spring Boot para reflejar la disponibilidad real.



## 🏗️ Lógica Detrás del Análisis
Para garantizar que el usuario observe datos reales, el sistema aplica una regla de negocio estricta en la capa de servicios:
* **Estado Ocupado:** Se confirma solo si existe un registro de ingreso sin fecha de salida.
* **Estado Disponible:** Se calcula restando la capacidad total del tipo de vehículo menos los registros activos.
* *Esto elimina el error humano de olvidar "liberar" un cupo manualmente.*

## ⚙️ Instalación para Desarrolladores

### Requisitos
- JDK 21
- MySQL 8.x
- Archivo `.env` configurado

### Ejecución

Sigue estos pasos para poner en marcha el sistema en tu entorno local. El proyecto está dividido en un **Backend (Spring Boot)** y un **Frontend (Next.js)**.

```bash
git clone https://github.com/SHF0987654321/Parqueadero.git
cd Parqueadero
```
## 2. Configuración de Entorno

Crea un archivo .env en la raíz de la carpeta Parqueadero y configura tus credenciales:
```
DB_URL=jdbc:mysql://localhost:3306/tu_base_de_datos
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
JWT_SECRET=tu_clave_secreta_para_seguridad
```
## 3. Levantar el Backend (Java 21)

Desde la carpeta raíz, ejecuta el Maven Wrapper:
```
./mvnw clean spring-boot:run
```
## 4. Levantar el Frontend (Next.js)

Abre una nueva terminal, navega a la carpeta del cliente e inicia el servidor de desarrollo:
```
cd frontend
npm install  # Ejecutar solo la primera vez
npm run dev
```
## 🚀 Roadmap de Evolución
Este proyecto está en constante mejora. Aquí puedes ver el estado actual y los próximos hitos:

* ✅ **MVP:** Lógica central de gestión de entradas y salidas.
* ✅ **Seguridad:** Implementación de autenticación con JWT y Spring Security 6.
* 🏗️ **Módulo de Reportes:** Generación de archivos PDF/Excel con el resumen de movimientos.
* ⏳ **Análisis de Tendencias:** Integración de gráficas para visualizar horas pico.
* 📅 **WebSockets (STOMP):** Actualización de ocupación en tiempo real.
* 🔔 **Notificaciones:** Sistema de alertas al celular para avisar disponibilidad.