import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/** Adaptador entre los objetos de dominio y la tabla Swing. */
public final class ModeloTablaGastos extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final String[] columnas = {"ID", "Concepto", "Categoría", "Monto"};
    private List<Gasto> gastos = List.of();

    public void actualizar(List<Gasto> gastos) {
        this.gastos = List.copyOf(gastos);
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return gastos.size(); }
    @Override public int getColumnCount() { return columnas.length; }
    @Override public String getColumnName(int columna) { return columnas[columna]; }
    @Override public Class<?> getColumnClass(int columna) {
        return columna == 3 ? BigDecimal.class : String.class;
    }
    @Override public Object getValueAt(int fila, int columna) {
        Gasto gasto = gastos.get(fila);
        return switch (columna) {
            case 0 -> gasto.getId().toString();
            case 1 -> gasto.getConcepto();
            case 2 -> gasto.getCategoria();
            case 3 -> gasto.getMonto();
            default -> throw new IndexOutOfBoundsException("Columna inválida");
        };
    }
}
