package eventos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal de la aplicacion.
 *
 * Los componentes se declaran como atributos de la clase para que, en las
 * siguientes etapas, los listeners de cada boton puedan leer los campos y
 * actualizar la tabla y el resultado. En esta etapa ningun boton tiene
 * todavia un evento asociado.
 */
public class VentanaGastos extends JFrame {

    // Opcion inicial del combo: obliga al usuario a elegir una categoria real.
    private static final String SIN_CATEGORIA = "Seleccione una categoría";

    private static final String[] CATEGORIAS = {
        SIN_CATEGORIA, "Alimentación", "Transporte", "Servicios",
        "Entretenimiento", "Salud", "Educación", "Otros"
    };

    private static final String[] COLUMNAS = {"N.°", "Concepto", "Categoría", "Monto"};

    // Entrada de datos del gasto (funcionalidad 1).
    private final JTextField txtConcepto = new JTextField(20);
    private final JTextField txtMonto = new JTextField(10);
    private final JComboBox<String> cmbCategoria = new JComboBox<>(CATEGORIAS);

    // Datos para las consultas (funcionalidades 5 y 6).
    private final JTextField txtMontoMinimo = new JTextField(10);
    private final JTextField txtBusqueda = new JTextField(15);

    // Botones: cada uno producira un evento distinto en las siguientes etapas.
    private final JButton btnRegistrar = new JButton("Registrar gasto");
    private final JButton btnMostrarTodos = new JButton("Mostrar todos");
    private final JButton btnCalcularTotal = new JButton("Calcular total");
    private final JButton btnMayorGasto = new JButton("Mayor gasto");
    private final JButton btnFiltrar = new JButton("Filtrar por monto");
    private final JButton btnBuscar = new JButton("Buscar concepto");

    // Presentacion de resultados (funcionalidad 2 y respuestas de las demas).
    // Las celdas no se editan a mano: los gastos solo cambian mediante los botones.
    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaGastos = new JTable(modeloTabla);
    private final JLabel lblResultado = new JLabel("Registre un gasto para comenzar.");

    public VentanaGastos() {
        super("Control de Gastos Personales - POE");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construirInterfaz();
    }

    /** Organiza la ventana en tres zonas: formularios arriba, tabla al centro y resultado abajo. */
    private void construirInterfaz() {
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 10, 0));
        panelSuperior.add(crearPanelRegistro());
        panelSuperior.add(crearPanelConsultas());

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(panelSuperior, BorderLayout.NORTH);
        contenido.add(crearPanelTabla(), BorderLayout.CENTER);
        contenido.add(crearPanelResultado(), BorderLayout.SOUTH);

        setContentPane(contenido);
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar gasto"));
        panel.add(new JLabel("Concepto:"));
        panel.add(txtConcepto);
        panel.add(new JLabel("Monto:"));
        panel.add(txtMonto);
        panel.add(new JLabel("Categoría:"));
        panel.add(cmbCategoria);
        panel.add(new JLabel());
        panel.add(btnRegistrar);
        return panel;
    }

    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Consultas"));
        panel.add(new JLabel("Monto mínimo:"));
        panel.add(txtMontoMinimo);
        panel.add(new JLabel("Buscar concepto:"));
        panel.add(txtBusqueda);
        panel.add(btnFiltrar);
        panel.add(btnBuscar);
        panel.add(btnCalcularTotal);
        panel.add(btnMayorGasto);
        return panel;
    }

    private JScrollPane crearPanelTabla() {
        tablaGastos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaGastos.getTableHeader().setReorderingAllowed(false);

        JScrollPane panel = new JScrollPane(tablaGastos);
        panel.setBorder(BorderFactory.createTitledBorder("Gastos registrados"));
        return panel;
    }

    private JPanel crearPanelResultado() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        panel.add(lblResultado, BorderLayout.CENTER);
        panel.add(btnMostrarTodos, BorderLayout.EAST);
        return panel;
    }
}
