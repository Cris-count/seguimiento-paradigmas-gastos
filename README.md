# seguimiento-paradigmas-gastos

## Paradigma orientado a eventos (POE) — Java Swing

**Integrante responsable:** Ingrid · **Rama:** `feature/paradigma-eventos` · **Carpeta:** `src/eventos/`

### Objetivo

Implementar el Sistema de Control de Gastos Personales aplicando la programación orientada a eventos. La aplicación presenta una interfaz gráfica y cada operación se ejecuta como respuesta a una acción del usuario: no hay un menú secuencial de consola.

### Descripción del paradigma

En la programación orientada a eventos, el orden en que se ejecuta el código lo determina la interacción del usuario y no una secuencia fija de instrucciones. El programa crea la ventana, registra **escuchadores** (*listeners*) en los componentes y queda a la espera. Cuando el usuario hace clic, el componente genera un **evento** y Swing ejecuta la **función de respuesta** (*callback*) asociada. Esa función llama a un **método controlador** que lee los datos, ejecuta la operación y actualiza la interfaz.

| Concepto | Dónde aparece en el código |
|---|---|
| Interfaz gráfica | `VentanaGastos` (hereda de `JFrame`) con `JPanel`, `JLabel`, `JTextField`, `JComboBox`, `JButton`, `JTable` y `JOptionPane` |
| Fuentes de eventos | Los seis botones de la ventana |
| Evento | El `ActionEvent` que genera cada clic |
| Listener y callback | Las expresiones lambda `evento -> ...` registradas con `addActionListener` en `registrarEventos()` |
| Controladores | `registrarGasto()`, `mostrarTodos()`, `calcularTotal()`, `mostrarMayorGasto()`, `filtrarPorMonto()` y `buscarPorConcepto()` |
| Hilo de eventos | `SwingUtilities.invokeLater` en `AppGastos` crea la ventana en el hilo de eventos de Swing |
| Respuesta visual | La tabla de gastos, el panel **Resultado** y los avisos de validación |

```java
private void registrarEventos() {
    btnRegistrar.addActionListener(evento -> registrarGasto());
    btnMostrarTodos.addActionListener(evento -> mostrarTodos());
    btnCalcularTotal.addActionListener(evento -> calcularTotal());
    btnMayorGasto.addActionListener(evento -> mostrarMayorGasto());
    btnFiltrar.addActionListener(evento -> filtrarPorMonto());
    btnBuscar.addActionListener(evento -> buscarPorConcepto());
}
```

### Tecnologías

- Java, JDK 11 o superior.
- Java Swing, que forma parte de la biblioteca estándar. No se usan dependencias externas.
- Los gastos se guardan en memoria mientras la aplicación está abierta. No hay base de datos ni archivos, y los datos se pierden al cerrar.

### Estructura

```text
src/eventos/
├── AppGastos.java      Punto de entrada: crea la ventana en el hilo de eventos y la muestra.
├── VentanaGastos.java  Interfaz, listeners y controladores de los seis eventos.
├── Gasto.java          Representa un gasto: concepto, monto y categoría.
└── GestorGastos.java   Almacena los gastos y realiza los cálculos. No conoce la interfaz.
```

La ventana se encarga de los eventos (escuchar, leer y mostrar) y el gestor se encarga de los datos (guardar, sumar, comparar, filtrar y buscar).

### Funcionalidades

| N.° | Funcionalidad | Botón (evento) | Controlador | Método del gestor | Respuesta visual |
|---|---|---|---|---|---|
| 1 | Registrar gasto | Registrar gasto | `registrarGasto()` | `registrar()` | Agrega la fila a la tabla y muestra una confirmación |
| 2 | Mostrar gastos | Mostrar todos | `mostrarTodos()` | `obtenerTodos()` | Muestra la tabla completa y la cantidad de gastos |
| 3 | Gasto total | Calcular total | `calcularTotal()` | `calcularTotal()` | Muestra el total acumulado en **Resultado** |
| 4 | Gasto de mayor valor | Mayor gasto | `mostrarMayorGasto()` | `obtenerMayorGasto()` | Muestra el concepto, la categoría y el monto |
| 5 | Filtrar por monto | Filtrar por monto | `filtrarPorMonto()` | `filtrarPorMontoMayorA()` | La tabla muestra solo los gastos con monto **estrictamente mayor** (`>`) |
| 6 | Buscar por concepto | Buscar concepto | `buscarPorConcepto()` | `buscarPorConcepto()` | La tabla muestra los gastos cuyo concepto contiene el texto, sin distinguir mayúsculas |

### Reglas de validación y presentación

- El concepto y la categoría son obligatorios.
- **Formato de los montos:** solo dígitos, sin separadores de miles. Los decimales se escriben con punto o coma y llevan como máximo dos cifras. Por ejemplo, `20000`, `3500.50` y `3500,50` son válidos. Una entrada como `20.000` se rechaza, porque podría leerse como veinte pesos. También se rechazan las letras, los signos, la notación científica y los valores especiales (`NaN`, `Infinity`).
- El monto de un gasto debe ser mayor que cero. El monto mínimo del filtro puede ser cero, pero no negativo.
- Los montos se muestran en pesos colombianos, con punto de miles y coma decimal: `$20.000`, `$3.500,50`.
- Filtrar y buscar crean listas nuevas y no modifican los gastos registrados. **Calcular total** y **Mayor gasto** usan siempre todos los gastos, aunque la tabla esté filtrada. **Mostrar todos** recupera la lista completa.
- Cuando no hay gastos, cada botón muestra un mensaje informativo en lugar de producir un error.
- Si dos gastos empatan en el mayor monto, se muestra el que se registró primero.
- La tabla no se puede editar a mano y se vacía antes de volver a llenarse, así que no se duplican filas.

### Compilar y ejecutar

Desde la carpeta raíz del repositorio, en Git Bash:

```bash
javac -encoding UTF-8 -d out src/eventos/*.java
java -cp out eventos.AppGastos
```

En PowerShell:

```powershell
javac -encoding UTF-8 -d out src\eventos\AppGastos.java src\eventos\VentanaGastos.java src\eventos\Gasto.java src\eventos\GestorGastos.java
java -cp out eventos.AppGastos
```

La opción `-encoding UTF-8` es necesaria para que las tildes se vean correctamente. La carpeta `out/` contiene los archivos compilados y no se sube al repositorio.

### Pruebas realizadas

Datos de prueba:

| Concepto | Monto | Categoría |
|---|---:|---|
| Almuerzo | 20000 | Alimentación |
| Transporte | 8000 | Transporte |
| Internet | 70000 | Servicios |
| Cine | 25000 | Entretenimiento |

| Prueba | Resultado esperado | Resultado obtenido |
|---|---|---|
| Mostrar todos | 4 gastos | 4 gastos |
| Calcular total | $123.000 | $123.000 |
| Mayor gasto | Internet, Servicios, $70.000 | Internet, Servicios, $70.000 |
| Filtrar por 20000 | Internet y Cine (Almuerzo queda excluido) | Internet y Cine |
| Filtrar por 70000 | Ningún gasto | "Ningún gasto supera $70.000." |
| Buscar "inter" | Internet | Internet |
| Buscar "ALMU" | Almuerzo | Almuerzo |
| Buscar "xyz" | Ningún resultado | "Ningún gasto contiene "xyz" en su concepto." |
| Concepto vacío, monto vacío, `abc`, `-5`, `0`, `20.000`, sin categoría | Aviso y ningún registro nuevo | Aviso y ningún registro nuevo |
| Monto mínimo vacío, `abc`, `-1`, `20.000` | Aviso y la tabla sin cambios | Aviso y la tabla sin cambios |
| Botones sin gastos registrados | Mensaje informativo, sin errores | Mensaje informativo, sin errores |
| Total y Mayor con la tabla filtrada | $123.000 e Internet | $123.000 e Internet |
| Montos con decimales (`3500.50`, `1200,5`) | $3.500,50 y $1.200,50 | $3.500,50 y $1.200,50 |

Durante el desarrollo, cada funcionalidad se probó manualmente en Windows antes de su commit. La versión final también se verificó con un programa de prueba auxiliar, que no forma parte del repositorio, que simula los clics sobre la interfaz: 78 comprobaciones correctas y 0 fallidas.

### Secuencia de desarrollo (commits)

| Commit | Descripción |
|---|---|
| `39602c4` | Ventana principal |
| `d45816d`, `7e20124` | Ajuste del repositorio y retiro de archivos generados |
| `44e8933` | Componentes gráficos |
| `abf631a` | Evento Registrar gasto |
| `f706f92` | Evento Mostrar todos |
| `cd8701c` | Evento Calcular total |
| `1589ca4` | Evento Mayor gasto |
| `ece753f` | Evento Filtrar por monto |
| `d86d926` | Evento Buscar concepto |

### Referencias

- Gómez Forero, D. T., Gómez Prada, U. E. y Viola Villamizar, J. B. (2018). *Fundamentos de programación*. Universidad Pontificia Bolivariana. http://hdl.handle.net/20.500.11912/4143
- Gómez Jiménez, E. y Aguilera Jinesta, R. G. (2022). *Metodología de la programación: conceptos, lógica e implementación* (1.ª ed.). Alpha Editorial.
- Oracle. (s. f.). *Lesson: Writing Event Listeners*. The Java Tutorials. https://docs.oracle.com/javase/tutorial/uiswing/events/index.html
