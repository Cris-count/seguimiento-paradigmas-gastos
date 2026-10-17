package eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Guarda los gastos en memoria mientras la aplicacion esta abierta.
 * No conoce la interfaz grafica: solo almacena y entrega los datos.
 */
public class GestorGastos {

    private final List<Gasto> gastos = new ArrayList<>();

    public void registrar(Gasto gasto) {
        gastos.add(gasto);
    }

    /** Suma los montos de todos los gastos registrados. Si no hay gastos, devuelve 0. */
    public double calcularTotal() {
        double total = 0;
        for (Gasto gasto : gastos) {
            total += gasto.getMonto();
        }
        return total;
    }

    /**
     * Busca el gasto con el monto mas alto entre todos los registrados.
     * Si varios gastos comparten el monto maximo, devuelve el primero que se registro,
     * porque solo se reemplaza al candidato cuando aparece un monto estrictamente mayor.
     * Si no hay gastos, devuelve null.
     */
    public Gasto obtenerMayorGasto() {
        Gasto mayor = null;
        for (Gasto gasto : gastos) {
            if (mayor == null || gasto.getMonto() > mayor.getMonto()) {
                mayor = gasto;
            }
        }
        return mayor;
    }

    /**
     * Devuelve una lista nueva con los gastos cuyo monto es estrictamente mayor
     * que el monto minimo indicado. La lista original no se modifica.
     */
    public List<Gasto> filtrarPorMontoMayorA(double montoMinimo) {
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getMonto() > montoMinimo) {
                resultado.add(gasto);
            }
        }
        return resultado;
    }

    /**
     * Devuelve una lista nueva con los gastos cuyo concepto contiene el texto buscado,
     * sin distinguir mayusculas de minusculas. La lista original no se modifica.
     */
    public List<Gasto> buscarPorConcepto(String texto) {
        String buscado = texto.toLowerCase(Locale.ROOT);
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getConcepto().toLowerCase(Locale.ROOT).contains(buscado)) {
                resultado.add(gasto);
            }
        }
        return resultado;
    }

    /** Devuelve los gastos en modo de solo lectura para que nadie los altere desde fuera. */
    public List<Gasto> obtenerTodos() {
        return Collections.unmodifiableList(gastos);
    }
}
