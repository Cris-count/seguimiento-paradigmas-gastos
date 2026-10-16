import java.util.Scanner;
import java.util.Locale;

public class ControlGastos {
    // Arreglos paralelos: el mismo indice identifica un gasto.
    static String[] conceptos = new String[10];
    static double[] montos = new double[10];
    static String[] categorias = new String[10];
    static int cantidad = 0;
    static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            switch (opcion) {
                case 1: registrarGasto(); break;
                case 2: listarGastos(); break;
                case 3:
                    System.out.printf(Locale.US, "Total acumulado: $%.2f%n",
                            calcularTotal(montos, cantidad));
                    break;
                case 4: mostrarMayor(); break;
                case 5:
                    double umbral = leerMonto("Mostrar gastos mayores a: $", true);
                    filtrarPorMonto(umbral);
                    break;
                case 6:
                    String palabra = leerTexto("Concepto o palabra clave: ");
                    buscarPorConcepto(palabra);
                    break;
                case 0: System.out.println("Hasta pronto."); break;
                default: System.out.println("Opcion invalida. Elija entre 0 y 6.");
            }
        } while (opcion != 0);
    }

    static void mostrarMenu() {
        System.out.println("\n=== CONTROL DE GASTOS PERSONALES ===");
        System.out.println("1. Registrar gasto");
        System.out.println("2. Mostrar todos los gastos");
        System.out.println("3. Calcular total acumulado");
        System.out.println("4. Mostrar gasto de mayor valor");
        System.out.println("5. Mostrar gastos mayores a un monto");
        System.out.println("6. Buscar gasto por concepto");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    static int leerOpcion() {
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El texto no puede estar vacio.");
        }
    }

    static double leerMonto(String mensaje, boolean permitirCero) {
        while (true) {
            System.out.print(mensaje);
            try {
                // Se acepta punto o coma decimal, sin separadores de miles.
                double valor = Double.parseDouble(entrada.nextLine().trim().replace(',', '.'));
                if (Double.isFinite(valor) && (valor > 0 || (permitirCero && valor == 0))) {
                    return valor;
                }
            } catch (NumberFormatException error) {
                // Se solicita otra entrada en lugar de terminar el programa.
            }
            System.out.println(permitirCero
                    ? "Ingrese un numero valido mayor o igual a cero."
                    : "Ingrese un numero valido mayor que cero.");
        }
    }

    static void ampliarCapacidad() {
        if (cantidad == conceptos.length) {
            int capacidad = conceptos.length * 2;
            String[] nuevosConceptos = new String[capacidad];
            double[] nuevosMontos = new double[capacidad];
            String[] nuevasCategorias = new String[capacidad];
            for (int i = 0; i < cantidad; i++) {
                nuevosConceptos[i] = conceptos[i];
                nuevosMontos[i] = montos[i];
                nuevasCategorias[i] = categorias[i];
            }
            conceptos = nuevosConceptos;
            montos = nuevosMontos;
            categorias = nuevasCategorias;
        }
    }

    static void registrarGasto() {
        String concepto = leerTexto("Concepto: ");
        double monto = leerMonto("Monto (sin separadores de miles): $", false);
        String categoria = leerTexto("Categoria (Alimentacion, Transporte, Servicios, etc.): ");
        ampliarCapacidad();
        conceptos[cantidad] = concepto;
        montos[cantidad] = monto;
        categorias[cantidad] = categoria;
        cantidad++;
        System.out.println("Gasto registrado correctamente.");
    }

    static void mostrarGasto(int indice) {
        System.out.printf(Locale.US, "%d. %s | $%.2f | Categoria: %s%n",
                indice + 1, conceptos[indice], montos[indice], categorias[indice]);
    }

    static void listarGastos() {
        if (cantidad == 0) {
            System.out.println("No hay gastos registrados.");
        } else {
            System.out.println("\n--- LISTADO DE GASTOS ---");
            for (int i = 0; i < cantidad; i++) {
                mostrarGasto(i);
            }
        }
    }

    static double calcularTotal(double[] valores, int numeroGastos) {
        double total = 0;
        for (int i = 0; i < numeroGastos; i++) {
            total += valores[i];
        }
        return total;
    }

    static void mostrarMayor() {
        if (cantidad == 0) {
            System.out.println("No hay gastos registrados.");
            return;
        }
        int indiceMayor = 0;
        for (int i = 1; i < cantidad; i++) {
            if (montos[i] > montos[indiceMayor]) {
                indiceMayor = i;
            }
        }
        System.out.println("Gasto de mayor valor (primero registrado en caso de empate):");
        mostrarGasto(indiceMayor);
    }

    static void filtrarPorMonto(double umbral) {
        boolean encontrado = false;
        for (int i = 0; i < cantidad; i++) {
            if (montos[i] > umbral) {
                mostrarGasto(i);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay gastos que superen ese monto.");
        }
    }

    static void buscarPorConcepto(String palabra) {
        boolean encontrado = false;
        String busqueda = palabra.toLowerCase(Locale.ROOT);
        for (int i = 0; i < cantidad; i++) {
            if (conceptos[i].toLowerCase(Locale.ROOT).contains(busqueda)) {
                mostrarGasto(i);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron coincidencias.");
        }
    }
}
