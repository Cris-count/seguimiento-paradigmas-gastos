package funcional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console boundary. The menu loop lives here; expense rules stay in pure functions.
 * Registering replaces the list with a new one and never mutates the previous list.
 */
public final class ConsolaGastos {

    private ConsolaGastos() {
    }

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            atenderMenu(entrada, List.of());
        }
    }

    private static void atenderMenu(Scanner entrada, List<Gasto> gastos) {
        while (true) {
            mostrarMenu();
            if (!entrada.hasNextLine()) {
                return;
            }
            String opcion = entrada.nextLine().strip();
            if (opcion.equals("0")) {
                return;
            }
            try {
                gastos = ejecutar(opcion, gastos, entrada);
            } catch (GastoInvalidoException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    private static List<Gasto> ejecutar(String opcion, List<Gasto> gastos, Scanner entrada) {
        if (opcion.equals("1")) {
            return registrarDesdeEntrada(gastos, entrada);
        }
        if (opcion.equals("2")) {
            mostrarDescripcion(gastos);
            return gastos;
        }
        if (opcion.equals("3")) {
            System.out.printf(Locale.US, "Total acumulado: %.2f%n", OperacionesGastos.total(gastos));
            return gastos;
        }
        if (opcion.equals("4")) {
            mostrarMayor(gastos);
            return gastos;
        }
        if (opcion.equals("5")) {
            double umbral = leerNumero(entrada, "Monto umbral");
            mostrarDescripcion(OperacionesGastos.mayoresQue(gastos, umbral));
            return gastos;
        }
        if (opcion.equals("6")) {
            String texto = leerLinea(entrada, "Concepto o palabra clave");
            mostrarDescripcion(OperacionesGastos.buscarPorConcepto(gastos, texto));
            return gastos;
        }
        throw new GastoInvalidoException("Opcion invalida: '" + opcion + "'.");
    }

    private static List<Gasto> registrarDesdeEntrada(List<Gasto> gastos, Scanner entrada) {
        String concepto = leerLinea(entrada, "Concepto");
        double monto = leerNumero(entrada, "Monto");
        String categoria = leerLinea(entrada, "Categoria");
        List<Gasto> actualizados = OperacionesGastos.registrar(gastos, concepto, monto, categoria);
        System.out.println("Gasto registrado.");
        return actualizados;
    }

    private static void mostrarMayor(List<Gasto> gastos) {
        Optional<Gasto> mayor = OperacionesGastos.mayor(gastos);
        if (mayor.isEmpty()) {
            System.out.println("No hay gastos registrados.");
            return;
        }
        System.out.println(OperacionesGastos.formatear(mayor.get()));
    }

    private static void mostrarDescripcion(List<Gasto> gastos) {
        List<String> lineas = OperacionesGastos.describir(gastos);
        if (lineas.isEmpty()) {
            System.out.println("No hay gastos para mostrar.");
            return;
        }
        lineas.forEach(System.out::println);
    }

    private static void mostrarMenu() {
        System.out.println("1. Registrar gasto");
        System.out.println("2. Mostrar listado de gastos");
        System.out.println("3. Calcular gasto total acumulado");
        System.out.println("4. Mostrar el gasto de mayor valor");
        System.out.println("5. Mostrar gastos mayores a un monto");
        System.out.println("6. Buscar gasto por concepto");
        System.out.println("0. Salir");
    }

    private static String leerLinea(Scanner entrada, String etiqueta) {
        System.out.println(etiqueta);
        if (!entrada.hasNextLine()) {
            throw new GastoInvalidoException("Entrada invalida: no hay datos para " + etiqueta + ".");
        }
        return entrada.nextLine();
    }

    private static double leerNumero(Scanner entrada, String etiqueta) {
        String texto = leerLinea(entrada, etiqueta).strip();
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException error) {
            throw new GastoInvalidoException(
                    etiqueta + " invalido: '" + texto + "'. Se esperaba un numero con punto decimal.");
        }
    }
}
