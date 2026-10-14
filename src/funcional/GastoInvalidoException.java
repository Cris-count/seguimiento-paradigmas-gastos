package funcional;

/**
 * Raised when an expense field or a query argument cannot be used.
 */
public class GastoInvalidoException extends IllegalArgumentException {

    public GastoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
