import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Logica de negocio independiente de la consola. */
public final class GestorGastos {
    private final List<Gasto> gastos = new ArrayList<>();

    public Gasto registrarGasto(String concepto, BigDecimal monto, String categoria) {
        Gasto gasto = new Gasto(concepto, monto, categoria);
        gastos.add(gasto);
        return gasto;
    }

    public List<Gasto> listarGastos() {
        return List.copyOf(gastos);
    }

    public BigDecimal calcularTotal() {
        return gastos.stream().map(Gasto::getMonto)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    public List<Gasto> obtenerGastosDeMayorValor() {
        if (gastos.isEmpty()) {
            return List.of();
        }
        BigDecimal maximo = gastos.stream().map(Gasto::getMonto)
                .max(BigDecimal::compareTo).orElseThrow();
        return gastos.stream().filter(g -> g.getMonto().compareTo(maximo) == 0).toList();
    }

    public List<Gasto> filtrarMayoresA(BigDecimal umbral) {
        Objects.requireNonNull(umbral, "El umbral es obligatorio.");
        if (umbral.signum() < 0) {
            throw new IllegalArgumentException("El umbral no puede ser negativo.");
        }
        return gastos.stream().filter(g -> g.getMonto().compareTo(umbral) > 0).toList();
    }

    public List<Gasto> buscarPorConcepto(String palabraClave) {
        if (palabraClave == null || palabraClave.isBlank()) {
            throw new IllegalArgumentException("La busqueda no puede estar vacia.");
        }
        String busqueda = palabraClave.strip().toLowerCase(Locale.ROOT);
        return gastos.stream()
                .filter(g -> g.getConcepto().toLowerCase(Locale.ROOT).contains(busqueda))
                .toList();
    }
}
