import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Entrada, salida y navegacion de la aplicacion. */
public final class MenuApp {
    private final GestorGastos gestor;
    private final Scanner teclado;

    public MenuApp(GestorGastos gestor, Scanner teclado) {
        this.gestor = gestor;
        this.teclado = teclado;
    }

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            new MenuApp(new GestorGastos(), teclado).ejecutar();
        }
    }

    public void ejecutar() {
        try {
            boolean continuar = true;
            while (continuar) {
                mostrarMenu();
                String opcion = leerLinea("Seleccione una opcion: ").strip();
                try {
                    switch (opcion) {
                        case "1" -> registrarGasto();
                        case "2" -> mostrarGastos(gestor.listarGastos(), "No hay gastos registrados.");
                        case "3" -> System.out.println("Total acumulado: " + moneda(gestor.calcularTotal()));
                        case "4" -> mostrarGastos(gestor.obtenerGastosDeMayorValor(), "No hay gastos registrados.");
                        case "5" -> mostrarGastos(gestor.filtrarMayoresA(leerMonto("Umbral: $ ", false)),
                                "No hay gastos que superen el umbral.");
                        case "6" -> mostrarGastos(gestor.buscarPorConcepto(leerTexto("Palabra clave: ")),
                                "No se encontraron coincidencias.");
                        case "7" -> continuar = false;
                        default -> System.out.println("Opcion invalida. Ingrese un numero del 1 al 7.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("Hasta pronto.");
        } catch (FinDeEntrada e) {
            System.out.println("\nEntrada finalizada. Hasta pronto.");
        }
    }

    private void mostrarMenu() {
        System.out.println("\n=== Sistema de Control de Gastos Personales ===");
        System.out.println("1. Registrar gasto");
        System.out.println("2. Mostrar listado de gastos");
        System.out.println("3. Calcular gasto total acumulado");
        System.out.println("4. Mostrar gastos de mayor valor");
        System.out.println("5. Mostrar gastos mayores a un monto");
        System.out.println("6. Buscar gasto por concepto");
        System.out.println("7. Salir");
    }

    private void registrarGasto() {
        String concepto = leerTexto("Concepto: ");
        BigDecimal monto = leerMonto("Monto: $ ", true);
        String categoria = leerTexto("Categoria (Alimentacion, Transporte, Servicios, etc.): ");
        Gasto gasto = gestor.registrarGasto(concepto, monto, categoria);
        System.out.println("Gasto registrado. ID: " + gasto.getId());
    }

    private String leerTexto(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje).strip();
            if (!texto.isBlank() && texto.codePoints().noneMatch(Character::isISOControl)) {
                return texto;
            }
            System.out.println("Ingrese un texto no vacio y sin caracteres de control.");
        }
    }

    private BigDecimal leerMonto(String mensaje, boolean positivo) {
        while (true) {
            String entrada = leerLinea(mensaje).strip();
            // Acepta punto o coma decimal; no acepta separadores de miles ni exponentes.
            if (!entrada.matches("[+-]?[0-9]+([.,][0-9]{1,2})?")) {
                System.out.println("Ingrese un numero con hasta dos decimales, sin separadores de miles.");
                continue;
            }
            BigDecimal monto = new BigDecimal(entrada.replace(',', '.'));
            if (positivo ? monto.signum() > 0 : monto.signum() >= 0) {
                return monto;
            }
            System.out.println(positivo ? "El monto debe ser mayor que cero." : "El umbral debe ser cero o mayor.");
        }
    }

    private String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!teclado.hasNextLine()) {
            throw new FinDeEntrada();
        }
        return teclado.nextLine();
    }

    private void mostrarGastos(List<Gasto> gastos, String mensajeVacio) {
        if (gastos.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        System.out.printf("%-36s | %-30s | %-20s | %s%n", "ID", "Concepto", "Categoria", "Monto");
        System.out.println("-".repeat(110));
        for (Gasto gasto : gastos) {
            System.out.printf("%-36s | %-30s | %-20s | %s%n", gasto.getId(),
                    gasto.getConcepto(), gasto.getCategoria(), moneda(gasto.getMonto()));
        }
    }

    private static String moneda(BigDecimal monto) {
        return String.format(Locale.US, "$ %,.2f", monto);
    }

    private static final class FinDeEntrada extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
