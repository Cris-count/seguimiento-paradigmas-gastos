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

    /** Devuelve los gastos en modo de solo lectura para que nadie los altere desde fuera. */
    public List<Gasto> obtenerTodos() {
        return Collections.unmodifiableList(gastos);
    }
}
