package eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /** Devuelve los gastos en modo de solo lectura para que nadie los altere desde fuera. */
    public List<Gasto> obtenerTodos() {
        return Collections.unmodifiableList(gastos);
    }
}
