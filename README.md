# PIGaleriaProyectos
# Guía de Configuración y Ejecución - PIGaleriaProyectos (Rama Irene)

Este archivo Markdown detalla los pasos técnicos realizados para corregir los errores de entorno, JDK y Classpath encontrados durante la configuración del proyecto.

## 1. Configuración del SDK (JDK 23)
El error principal se originó por rutas de JDK inexistentes heredadas del repositorio original (pertenecientes al usuario "308" (directorio del usuario original)).

* **Ruta del SDK:** (crt + alt + shift + s) configurar el proyecto para usar **Oracle OpenJDK 23.0.1**. 
* **Language Level:** Se estableció en `23 (Preview) - Primitive types in patterns, implicitly declared classes, etc.` para dar soporte a las características de vista previa de Java 23.
* **Limpieza de Entorno:** En SDK eliminar las entradas de SDK "fantasmas" (como *ms-21*) que apuntaban a directorios locales del usuario original.

## 2. Sincronización con Maven
Para que IntelliJ reconozca correctamente las dependencias de JavaFX y la estructura de paquetes:

1.  Hacer clic derecho sobre el archivo `pom.xml`.
2.  Seleccionar **Maven > Reload Project**. o en la M del panel derecho.
3.  Verificar que la carpeta `src/main/java` esté marcada de color azul (**Sources Root**).

## 3. Limpieza de Compilación
Es fundamental eliminar los archivos binarios (`.class`) antiguos que generan el error `ClassNotFoundException`.

* **Comando:** Ejecutar en la terminal de IntelliJ:
    ```bash
    mvn clean compile
    ```
  o en Lifecycle, correr el apartado clean

## 4. Ejecución del Proyecto

### Método A: Usando Maven (Recomendado)
Este método es el más seguro para proyectos JavaFX ya que gestiona automáticamente el path de los módulos.

1.  Abrir la pestaña lateral de **Maven**.
2.  Navegar a **Plugins > javafx**.
3.  Hacer doble clic en **javafx:run**.

### Método B: Ejecución desde el Código Fuente
Si se prefiere usar el botón de Play de IntelliJ:

1.  Navegar en el árbol del proyecto hasta `src/main/java/galeria/app/MainApp.java`.
2.  Hacer clic derecho sobre el archivo `.java` (no el `.class` de la carpeta target).
3.  Seleccionar **Run 'MainApp.main()'**.

---
**Resultado Final:** El proyecto ahora compila y ejecuta correctamente utilizando el compilador de Java 23 y las dependencias gestionadas por Maven.