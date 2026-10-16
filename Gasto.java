import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** Modelo de un gasto, con identidad inmutable y datos validados. */
public final class Gasto {
    private final UUID id;
    private String concepto;
    private BigDecimal monto;
    private String categoria;

    public Gasto(String concepto, BigDecimal monto, String categoria) {
        this.id = UUID.randomUUID();
        setConcepto(concepto);
        setMonto(monto);
        setCategoria(categoria);
    }

    public UUID getId() { return id; }
    public String getConcepto() { return concepto; }
    public BigDecimal getMonto() { return monto; }
    public String getCategoria() { return categoria; }

    public void setConcepto(String concepto) {
        this.concepto = validarTexto(concepto, "El concepto");
    }

    public void setCategoria(String categoria) {
        this.categoria = validarTexto(categoria, "La categoria");
    }

    public void setMonto(BigDecimal monto) {
        Objects.requireNonNull(monto, "El monto es obligatorio.");
        if (monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        try {
            // No se redondea silenciosamente un importe ingresado por el usuario.
            this.monto = monto.setScale(2, java.math.RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("El monto admite hasta dos decimales.");
        }
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        String limpio = texto.strip();
        if (limpio.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(campo + " no admite caracteres de control.");
        }
        return limpio;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s | %s | %s | $ %,.2f",
                id, concepto, categoria, monto);
    }
}
