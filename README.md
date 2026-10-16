# Sistema de Control de Gastos Personales

Aplicacion de consola en Java, sin dependencias externas.

## Clases

```text
MenuApp ──> GestorGastos ──> Gasto
```

- `Gasto`: encapsula un UUID inmutable, concepto, monto y categoria; valida sus setters y ofrece una representacion textual.
- `GestorGastos`: registra, lista, suma, obtiene todos los maximos, filtra y busca; no realiza entrada/salida.
- `MenuApp`: administra el menu, lee datos, informa errores y muestra resultados.

Los importes usan `BigDecimal` para evitar errores de precision binaria. Se admiten hasta dos decimales. La categoria es texto libre obligatorio. Los gastos se mantienen en memoria durante la ejecucion y no se guardan al salir.

## Compilar y ejecutar

Instale un JDK 17 o superior y abra una terminal en esta carpeta.

```powershell
javac -encoding UTF-8 Gasto.java GestorGastos.java MenuApp.java
java MenuApp
```

El menu ofrece las seis operaciones y la opcion 7 para salir. Para introducir montos, utilice `1250.50` o `1250,50`, sin separadores de miles. Los registros requieren montos mayores que cero; el umbral permite cero. La busqueda por concepto ignora mayusculas y minusculas. Si varios gastos tienen el monto maximo, se muestran todos.

Las entradas incorrectas permiten reintentar. El fin de entrada tambien termina limpiamente la aplicacion.

## Interfaz grafica (Swing)

```powershell
javac -encoding UTF-8 -d build Gasto.java GestorGastos.java ModeloTablaGastos.java VentanaGastos.java AppGrafica.java
java -cp build AppGrafica
```

En IntelliJ, abra `AppGrafica.java` y ejecute su metodo `main`.
La ventana permite registrar gastos, consultar el total, buscar conceptos, filtrar por umbral y mostrar todos los empates del gasto maximo. La tabla se puede ordenar haciendo clic en sus encabezados.

`AppGrafica` inicia la aplicacion; `VentanaGastos` maneja la presentacion y los eventos; `ModeloTablaGastos` adapta los objetos para una tabla no editable. Ambas interfaces reutilizan `GestorGastos` y `Gasto`, conservando la separacion POO. Los datos siguen siendo temporales y cada ejecucion inicia una sesion independiente.
