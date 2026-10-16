package eventos;

/**
 * Representa un gasto registrado por el usuario.
 * Sus datos se asignan al crearlo y no cambian despues.
 */
public class Gasto {

    private final String concepto;
    private final double monto;
    private final String categoria;

    public Gasto(String concepto, double monto, String categoria) {
        this.concepto = concepto;
        this.monto = monto;
        this.categoria = categoria;
    }

    public String getConcepto() {
        return concepto;
    }

    public double getMonto() {
        return monto;
    }

    public String getCategoria() {
        return categoria;
    }
}
