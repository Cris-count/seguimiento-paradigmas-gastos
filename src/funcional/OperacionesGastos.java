package funcional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Pure expense operations. Each function returns a new value and leaves the
 * input list unchanged. Totals, filters and lookups use map, filter and reduce.
 */
public final class OperacionesGastos {

    private OperacionesGastos() {
    }

    public static List<Gasto> registrar(List<Gasto> gastos, String concepto, double monto, String categoria) {
        String conceptoLimpio = textoRequerido(concepto, "Concepto");
        String categoriaLimpia = textoRequerido(categoria, "Categoria");
        validarMontoPositivo(monto);
        Gasto nuevo = new Gasto(conceptoLimpio, monto, categoriaLimpia);
        return Stream.concat(gastos.stream(), Stream.of(nuevo)).toList();
    }

    public static List<Gasto> listar(List<Gasto> gastos) {
        return List.copyOf(gastos);
    }

    public static List<String> describir(List<Gasto> gastos) {
        return listar(gastos).stream().map(OperacionesGastos::formatear).toList();
    }

    public static double total(List<Gasto> gastos) {
        return listar(gastos).stream().map(Gasto::monto).reduce(0.0, Double::sum);
    }

    public static Optional<Gasto> mayor(List<Gasto> gastos) {
        return listar(gastos).stream().reduce(OperacionesGastos::conservarMayorMonto);
    }

    public static List<Gasto> mayoresQue(List<Gasto> gastos, double umbral) {
        validarNumeroFinito(umbral, "Umbral");
        return listar(gastos).stream().filter(gasto -> gasto.monto() > umbral).toList();
    }

    public static List<Gasto> buscarPorConcepto(List<Gasto> gastos, String texto) {
        String clave = textoRequerido(texto, "Texto de busqueda").toLowerCase(Locale.ROOT);
        return listar(gastos).stream()
                .filter(gasto -> gasto.concepto().toLowerCase(Locale.ROOT).contains(clave))
                .toList();
    }

    public static String formatear(Gasto gasto) {
        return String.format(
                Locale.US,
                "%s | %.2f | %s",
                gasto.concepto(),
                gasto.monto(),
                gasto.categoria());
    }

    private static Gasto conservarMayorMonto(Gasto actual, Gasto candidato) {
        if (actual.monto() >= candidato.monto()) {
            return actual;
        }
        return candidato;
    }

    private static String textoRequerido(String texto, String campo) {
        if (texto == null || texto.strip().isEmpty()) {
            throw new GastoInvalidoException(campo + " invalido: el texto esta vacio.");
        }
        return texto.strip();
    }

    private static void validarMontoPositivo(double monto) {
        validarNumeroFinito(monto, "Monto");
        if (monto <= 0) {
            throw new GastoInvalidoException(
                    "Monto invalido: " + monto + ". El monto debe ser mayor que 0.");
        }
    }

    private static void validarNumeroFinito(double numero, String campo) {
        if (Double.isNaN(numero) || Double.isInfinite(numero)) {
            throw new GastoInvalidoException(campo + " invalido: " + numero + ".");
        }
    }
}
