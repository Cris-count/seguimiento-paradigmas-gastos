# Control de gastos personales

Aplicación de consola, en Java, para registrar y consultar gastos. Esta entrega implementa solo el paradigma funcional.

## Ejecutar

Requiere Java 25.

```text
javac -encoding UTF-8 -d out src/funcional/*.java
java -cp out funcional.ConsolaGastos
```

El menú cubre las seis operaciones: registrar, listar, total acumulado, gasto de mayor valor, gastos mayores a un monto y búsqueda por concepto.

Los montos se escriben con punto decimal, por ejemplo `25.50`.

## Paradigma funcional

El dominio no modifica los datos que recibe. `Gasto` es un `record` inmutable: concepto, monto y categoría quedan fijos al crearlo. Registrar un gasto devuelve una lista nueva; la lista anterior permanece igual. La consola solo sustituye su referencia con esa lista nueva.

Las reglas de negocio están en funciones puras: el resultado depende de los argumentos y no hay variables acumuladoras ni ciclos `for` o `while`. El único ciclo está en el menú, porque tiene que volver a leer la opción del usuario.

Las transformaciones usan funciones de orden superior, con expresiones lambda o referencias a funciones:

- `map` extrae los montos para el total y convierte cada gasto en una línea del listado.
- `filter` arma los subconjuntos: gastos por encima de un umbral y coincidencias por subcadena en el concepto, sin distinguir mayúsculas.
- `reduce` suma los montos con `Double::sum` y elige el gasto de mayor valor. Si todavía no hay gastos, el total es `0` y el mayor valor se informa como vacío.

## Consulta bibliográfica

Pendiente. El equipo debe pegar aquí el resumen o el comprobante de la consulta sobre la teoría de paradigmas de programación, hecho en las bases de datos bibliográficas o en la biblioteca institucional.
