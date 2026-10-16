# Bitácora — Programación Orientada a Eventos (POE)

Rama: `feature/paradigma-eventos` · Responsable: Ingrid · Tecnología: Java + Swing

Cada etapa se registra en orden. Si se corrige algo de una etapa anterior, se agrega como una entrada nueva.

| Etapa | Objetivo | Archivos | Cambio realizado | Concepto aprendido | Prueba ejecutada | Resultado | Commit |
|---|---|---|---|---|---|---|---|
| 0 | Diagnóstico del repositorio | Ninguno | Revisión de ramas, estado de Git, convenciones (`src/<paradigma>/`) y herramientas | Diferencia entre carpeta y rama; ramas locales y remotas | `git status`, `git branch --show-current`, `java -version`, `javac -version` (13-10-2026) | Correcto. Árbol limpio en `feature/paradigma-eventos`. En la terminal local solo hay Java 11 (JRE) sin `javac` | No aplica |
| 1 | Crear la ventana principal | `src/eventos/AppGastos.java`, `src/eventos/VentanaGastos.java`, `docs/bitacora-poe.md` | Punto de entrada `main` que crea y muestra un `JFrame` de 800x600, centrado y que termina el programa al cerrarse | `package`, `import`, clase, objeto, `new`, constructor, `extends`, `super`, `static`, `void`, hilo de eventos (EDT), callback | Compilación con `javac --release 11` y ejecución con pantalla virtual: título, tamaño 800x600, visible y cierre `EXIT_ON_CLOSE` verificados; captura de pantalla obtenida. Windows (15-10-2026, reportada por Ingrid): la aplicación se ejecutó, la ventana se abrió con el título "Control de Gastos Personales - POE" y la interfaz se visualizó | Correcto. No se registraron las versiones de Java/javac de Windows ni la prueba de cierre con la X en Windows | Pendiente |
