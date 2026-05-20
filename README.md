# Sistema de Gestión de Rutinas de Entrenamiento Personalizadas
**Paradigmas de Programación – PTEC102 | Universidad Andrés Bello**

---

## Descripción general

Sistema con interfaz gráfica (Java Swing) que permite a instructores deportivos generar rutinas de entrenamiento personalizadas para sus clientes, cargando ejercicios desde un archivo CSV y filtrándolos según tipo e intensidad, respetando la restricción de no repetición en semanas consecutivas.

---

## Requisitos para ejecutar

- **Java JDK 11 o superior** instalado en el sistema
- No requiere librerías externas (solo Java estándar + Swing)

Para verificar que Java está instalado, abre una terminal y ejecuta:
```
java -version
```

---

## Instrucciones de ejecución

### Opción 1 – Desde un IDE (IntelliJ IDEA, Eclipse, NetBeans)

1. Abrir el IDE y crear un nuevo proyecto Java
2. Copiar las carpetas `backend/` y `frontend/` dentro del directorio `src/` del proyecto
3. Ejecutar la clase principal: `frontend.gui.VentanaPrincipal`

### Opción 2 – Desde la terminal (línea de comandos)

1. Abrir una terminal en la carpeta raíz del proyecto (donde están las carpetas `backend/` y `frontend/`)
2. Compilar todos los archivos:
```
javac -encoding UTF-8 backend/modelo/Ejercicio.java backend/eventos/Suscriptor.java backend/logica/GestorRutinas.java frontend/gui/VentanaResumen.java frontend/gui/VentanaRevision.java frontend/gui/VentanaGeneracion.java frontend/gui/VentanaPrincipal.java
```
3. Ejecutar el sistema:
```
java frontend.gui.VentanaPrincipal
```

---

## Cómo usar el sistema

1. Al abrir el programa, aparece la **ventana principal**
2. Ingresar el número de semana actual usando el selector (1–52)
3. Hacer clic en **"Seleccionar Archivo de Ejercicios"** y elegir el archivo CSV
4. El sistema mostrará las estadísticas de los ejercicios cargados
5. Hacer clic en **"Generar Rutina de Entrenamiento"**
6. En la ventana de generación, ingresar:
   - Cantidad de ejercicios cardiovasculares deseados
   - Cantidad de ejercicios de fuerza deseados
   - Nivel de intensidad requerido
7. Navegar por la rutina ejercicio a ejercicio con los botones **Anterior / Siguiente**
8. Al llegar al último ejercicio, hacer clic en **"Ver Resumen"** para ver el resumen final

---

## Formato del archivo de ejercicios (CSV)

El archivo debe ser un `.csv` sin encabezado, con **7 campos separados por coma** en el siguiente orden:

```
CÓDIGO, NOMBRE, TIPO, INTENSIDAD, TIEMPO_MINUTOS, DESCRIPCIÓN, ÚLTIMA_SEMANA_USO
```

| Campo | Descripción | Valores válidos |
|---|---|---|
| CÓDIGO | Identificador único del ejercicio | Texto (ej: E001) |
| NOMBRE | Nombre del ejercicio | Texto |
| TIPO | Tipo de ejercicio | `Cardiovascular` o `Fuerza` |
| INTENSIDAD | Nivel de intensidad | `Básico`, `Intermedio`, `Avanzado`, `Alto rendimiento` |
| TIEMPO_MINUTOS | Duración estimada | Número entero positivo |
| DESCRIPCIÓN | Descripción de ejecución | Texto |
| ÚLTIMA_SEMANA_USO | Semana en que fue usado por última vez | Número entero (1–52) |

### Ejemplo de líneas válidas:
```
E001, Caminata en cinta, Cardiovascular, Básico, 20, Caminata a ritmo constante sin inclinación, 5
E020, Flexiones con rodillas, Fuerza, Básico, 10, Flexiones de brazos apoyando las rodillas, 4
E035, Levantamiento olímpico, Fuerza, Alto rendimiento, 30, Arrancada completa desde el suelo, 2
```

> **Nota:** El sistema también acepta archivos con una línea de encabezado que comience con la palabra "codigo" o "código" — la ignorará automáticamente.

---

## Validaciones del sistema

El sistema maneja los siguientes errores mediante excepciones:

- **Archivo inexistente:** muestra mensaje de error si la ruta seleccionada no existe
- **Formato incorrecto:** si alguna línea no tiene exactamente 7 campos, se informa el número de línea con error
- **Datos inválidos:** si el tiempo o semana no son números enteros, se notifica el error
- **Sin ejercicios suficientes:** si no hay ejercicios disponibles que cumplan los filtros, se informa al usuario sin cerrar la ventana de generación

---

## Estructura del proyecto

```
Tarea2-Paradigmas/
│
├── backend/
│   ├── modelo/
│   │   └── Ejercicio.java          # Clase que representa un ejercicio
│   ├── eventos/
│   │   └── Suscriptor.java         # Interfaz del patrón Observador
│   └── logica/
│       └── GestorRutinas.java      # Lógica principal del sistema
│
├── frontend/
│   └── gui/
│       ├── VentanaPrincipal.java   # Ventana de inicio y carga
│       ├── VentanaGeneracion.java  # Ventana para configurar la rutina
│       ├── VentanaRevision.java    # Revisión ejercicio por ejercicio
│       └── VentanaResumen.java     # Resumen final de la rutina
│
├── ejercicios.csv                  # Archivo de datos de ejemplo
└── README.md                       # Este archivo
```

---

## Alcances y supuestos

- El sistema asume que la semana actual debe ser ingresada manualmente por el usuario al iniciar (selector de semana en la ventana principal). Esto permite flexibilidad sin depender del reloj del sistema.
- La restricción de "no repetición en semanas consecutivas" se aplica comparando la semana actual ingresada con la última semana de uso de cada ejercicio. Si la diferencia es mayor a 1, el ejercicio está disponible.
- Al generar una rutina, la semana de uso de los ejercicios seleccionados se actualiza automáticamente en memoria (no se persiste en el archivo CSV original).
- El sistema soporta archivos CSV con o sin línea de encabezado.
- Se asume que el archivo CSV usa codificación UTF-8.
- Los campos de texto como nombre y descripción pueden contener espacios, pero no comas (ya que la coma es el delimitador).
- El sistema fue desarrollado y probado con Java 21.
