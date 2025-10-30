# Trabajo Práctico - Consultas JPQL

Proyecto universitario que implementa 14 consultas JPQL (Java Persistence Query Language) sobre un modelo de entidades JPA. Este repositorio corresponde al Trabajo Práctico de la materia [Nombre de la Materia].

## 👥 Integrantes

* Franco D'Agostino
* Belen Carmona
* Juan Nieves
* Adolfo Quintana
* Luciano Paulucci

## 💻 Tecnologías Utilizadas

* **Java:** Lenguaje principal del proyecto.
* **JPA (Hibernate):** Para el mapeo Objeto-Relacional (ORM) y la persistencia de datos.
* **JPQL:** Lenguaje de consulta utilizado para interactuar con las entidades.
* **H2 Database:** Base de datos en memoria para una rápida configuración y prueba.
* **Lombok:** Librería para reducir el código boilerplate (getters, setters, constructores).
* **Gradle:** Como gestor de dependencias y build del proyecto.

## 🚀 Cómo Ejecutar

El proyecto está configurado para ejecutarse con una base de datos H2 en memoria. Esto significa que **no se necesita ninguna configuración de base de datos externa**.

1.  **Clonar el Repositorio:**
    ```bash
    git clone [URL_DE_TU_REPOSITORIO]
    ```

2.  **Abrir el Proyecto:**
    Abre el proyecto con tu IDE de preferencia (Ej. IntelliJ IDEA o Eclipse). El gestor de dependencias (Gradle) descargará todo lo necesario automáticamente.

3.  **Ejecutar el Proyecto:**
    Busca la clase `MainTrabajoPractico.java` (ubicada en `src/main/java/org/example/`) y haz clic derecho > **Run 'MainTrabajoPractico.main()'**.

4.  **Ver la Salida:**
    Revisa la consola de tu IDE. El programa primero poblará la base de datos en memoria con datos de ejemplo y luego ejecutará las 14 consultas del Trabajo Práctico, imprimiendo sus resultados.

