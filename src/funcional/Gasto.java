package funcional;

/**
 * Immutable expense. Concept, amount and category stay fixed after creation.
 */
public record Gasto(String concepto, double monto, String categoria) {
}
